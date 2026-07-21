
# =====================================================
# SECTION 1 : IMPORT LIBRARIES
# =====================================================
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import logging
import os
from datetime import datetime


# =====================================================
# SECTION 2 : CONFIGURE LOGGING
# =====================================================
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

DATA_DIR = os.path.join(BASE_DIR, "data")
REPORTS_DIR = os.path.join(BASE_DIR, "reports")
CHARTS_DIR = os.path.join(BASE_DIR, "charts")
LOGS_DIR = os.path.join(BASE_DIR, "logs")

os.makedirs(DATA_DIR, exist_ok=True)
os.makedirs(REPORTS_DIR, exist_ok=True)
os.makedirs(CHARTS_DIR, exist_ok=True)
os.makedirs(LOGS_DIR, exist_ok=True)

LOG_FILE_PATH = os.path.join(LOGS_DIR, "portfolio.log")

logging.basicConfig(
    filename=LOG_FILE_PATH,
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)

logging.info("Program Started")


# =====================================================
# SECTION 3 : EXCEPTION HANDLING FOR CSV READING
# =====================================================
def read_csv_file(filename):
    file_path = os.path.join(DATA_DIR, filename)

    try:
        dataframe = pd.read_csv(file_path)
        return dataframe

    except FileNotFoundError:
        print("File not found:", file_path)
        logging.error("File not found: %s", file_path)
        return pd.DataFrame()

    except pd.errors.EmptyDataError:
        print("CSV file is empty:", file_path)
        logging.error("CSV file is empty: %s", file_path)
        return pd.DataFrame()

    except Exception as error_message:
        print("Error reading file:", file_path)
        print("Details:", error_message)
        logging.error("Unexpected error reading %s: %s", file_path, str(error_message))
        return pd.DataFrame()


def find_column_name(dataframe, possible_names):
    for name in possible_names:
        if name in dataframe.columns:
            return name
    return None


def normalize_text_columns(dataframe):
    for column_name in dataframe.columns:
        if dataframe[column_name].dtype == "object" or pd.api.types.is_string_dtype(dataframe[column_name]):
            dataframe[column_name] = dataframe[column_name].astype("string").str.strip()
    return dataframe


def create_id_key(series_data):
    string_series = series_data.astype(str)
    key_series = string_series.str.extract(r"(\d+)", expand=False)
    key_series = pd.to_numeric(key_series, errors="coerce")
    return key_series


# =====================================================
# SECTION 4 : READ CSV FILES
# =====================================================
investors_df = read_csv_file("investors.csv")
funds_df = read_csv_file("funds.csv")
transactions_df = read_csv_file("transactions.csv")
nav_history_df = read_csv_file("nav_history.csv")

logging.info("Files Loaded")


# =====================================================
# SECTION 5 : OBJECT ORIENTED PROGRAMMING
# =====================================================
class FundPortfolio:

    def __init__(self, investors, funds, transactions, nav_history):
        self.investors = investors.copy()
        self.funds = funds.copy()
        self.transactions = transactions.copy()
        self.nav_history = nav_history.copy()

        self.portfolio_df = pd.DataFrame()

        self.portfolio_report_df = pd.DataFrame()
        self.investor_report_df = pd.DataFrame()
        self.fund_report_df = pd.DataFrame()

        self.investor_analysis_results = {}
        self.fund_analysis_results = {}
        self.metrics_results = {}

    # SECTION 6 : DATA CLEANING
    def clean_data(self):

        self.investors = normalize_text_columns(self.investors)
        self.funds = normalize_text_columns(self.funds)
        self.transactions = normalize_text_columns(self.transactions)
        self.nav_history = normalize_text_columns(self.nav_history)

        annual_income_col = find_column_name(self.investors, ["AnnualIncome", "Annual Income"])
        risk_profile_col = find_column_name(self.investors, ["RiskProfile", "Risk Profile"])

        if annual_income_col is not None:
            annual_income_median = self.investors[annual_income_col].median()
            self.investors[annual_income_col] = self.investors[annual_income_col].fillna(annual_income_median)

        if risk_profile_col is not None:
            self.investors[risk_profile_col] = self.investors[risk_profile_col].fillna("Moderate")

        expense_ratio_col = find_column_name(self.funds, ["ExpenseRatio", "Expense Ratio"])

        if expense_ratio_col is not None:
            expense_ratio_mean = self.funds[expense_ratio_col].mean()
            self.funds[expense_ratio_col] = self.funds[expense_ratio_col].fillna(expense_ratio_mean)

        nav_col = find_column_name(self.nav_history, ["NAV", "Nav"])
        date_col = find_column_name(self.nav_history, ["Date", "NAVDate", "NavDate"])

        if nav_col is not None and date_col is not None and "FundID" in self.nav_history.columns:
            self.nav_history[date_col] = pd.to_datetime(self.nav_history[date_col], errors="coerce")

            self.nav_history = self.nav_history.sort_values(["FundID", date_col])

            self.nav_history[nav_col] = self.nav_history.groupby("FundID")[nav_col].ffill()

            nav_median = self.nav_history[nav_col].median()
            self.nav_history[nav_col] = self.nav_history[nav_col].fillna(nav_median)

        self.transactions = self.transactions.drop_duplicates()

        if "TransactionDate" in self.transactions.columns:
            self.transactions["TransactionDate"] = pd.to_datetime(
                self.transactions["TransactionDate"],
                errors="coerce"
            )

        if "Units" in self.transactions.columns:
            self.transactions["Units"] = pd.to_numeric(self.transactions["Units"], errors="coerce").fillna(0)

        if "Amount" in self.transactions.columns:
            self.transactions["Amount"] = pd.to_numeric(self.transactions["Amount"], errors="coerce").fillna(0)

        logging.info("Missing Values Cleaned")
        print("Data cleaning completed.")

    # SECTION 7 : MERGE DATA
    def merge_data(self):

        if "InvestorID" in self.investors.columns:
            self.investors["InvestorKey"] = create_id_key(self.investors["InvestorID"])

        if "InvestorID" in self.transactions.columns:
            self.transactions["InvestorKey"] = create_id_key(self.transactions["InvestorID"])

        if "FundID" in self.funds.columns:
            self.funds["FundKey"] = create_id_key(self.funds["FundID"])

        if "FundID" in self.transactions.columns:
            self.transactions["FundKey"] = create_id_key(self.transactions["FundID"])

        if "FundID" in self.nav_history.columns:
            self.nav_history["FundKey"] = create_id_key(self.nav_history["FundID"])

        merged_step_1 = pd.merge(
            self.transactions,
            self.investors,
            on="InvestorKey",
            how="left",
            suffixes=("_txn", "_inv")
        )

        merged_step_2 = pd.merge(
            merged_step_1,
            self.funds,
            on="FundKey",
            how="left",
            suffixes=("", "_fund")
        )

        nav_col = find_column_name(self.nav_history, ["NAV", "Nav"])
        date_col = find_column_name(self.nav_history, ["Date", "NAVDate", "NavDate"])

        latest_nav = pd.DataFrame()
        if nav_col is not None and date_col is not None:
            latest_nav = self.nav_history[["FundKey", date_col, nav_col]].copy()
            latest_nav = latest_nav.sort_values(["FundKey", date_col])
            latest_nav = latest_nav.groupby("FundKey").tail(1)
            latest_nav = latest_nav.rename(columns={nav_col: "LatestNAV", date_col: "LatestNAVDate"})

        self.portfolio_df = pd.merge(
            merged_step_2,
            latest_nav,
            on="FundKey",
            how="left"
        )

        print("Data merge completed.")

    # SECTION 8 : NUMPY CALCULATIONS
    def numpy_analysis(self):
        print("\nNumPy Analysis Results")

        annual_income_col = find_column_name(self.portfolio_df, ["AnnualIncome", "Annual Income"])
        amount_col = find_column_name(self.portfolio_df, ["Amount", "Investment Amount"])
        nav_col = find_column_name(self.portfolio_df, ["NAV", "Nav"])

        if amount_col is not None:
            amount_array = self.portfolio_df[amount_col].dropna().values
            mean_investment = np.mean(amount_array) if len(amount_array) > 0 else 0
            print("Mean Investment =", mean_investment)
        else:
            mean_investment = 0
            print("Mean Investment = Column not found")

        if annual_income_col is not None:
            income_array = self.portfolio_df[annual_income_col].dropna().values
            median_income = np.median(income_array) if len(income_array) > 0 else 0
            print("Median Investor Income =", median_income)
        else:
            median_income = 0
            print("Median Investor Income = Column not found")

        if nav_col is not None:
            nav_array = self.portfolio_df[nav_col].dropna().values
            nav_std_dev = np.std(nav_array) if len(nav_array) > 0 else 0
            print("Standard Deviation of NAV =", nav_std_dev)
        else:
            nav_std_dev = 0
            print("Standard Deviation of NAV = Column not found")

        fund_return_values = []
        if "FundKey" in self.nav_history.columns:
            nav_history_copy = self.nav_history.copy()
            nav_history_copy = nav_history_copy.sort_values(["FundKey", "Date"])

            unique_fund_keys = nav_history_copy["FundKey"].dropna().unique()
            for fund_key in unique_fund_keys:
                one_fund_data = nav_history_copy[nav_history_copy["FundKey"] == fund_key]
                if len(one_fund_data) > 1:
                    first_nav = one_fund_data.iloc[0]["NAV"]
                    last_nav = one_fund_data.iloc[-1]["NAV"]
                    if first_nav != 0:
                        fund_return_percent = ((last_nav - first_nav) / first_nav) * 100
                        fund_return_values.append(fund_return_percent)

        if len(fund_return_values) > 0:
            fund_return_array = np.array(fund_return_values)
            percentile_90 = np.percentile(fund_return_array, 90)
            percentile_95 = np.percentile(fund_return_array, 95)
            print("90 Percentile Fund Returns =", percentile_90)
            print("95 Percentile Fund Returns =", percentile_95)
        else:
            percentile_90 = 0
            percentile_95 = 0
            print("90 Percentile Fund Returns = Not enough data")
            print("95 Percentile Fund Returns = Not enough data")

        corr_income_amount = np.nan
        corr_income_avg_nav = np.nan
        corr_amount_avg_nav = np.nan

        if annual_income_col is not None and amount_col is not None:
            income_values = self.portfolio_df[annual_income_col].fillna(0).values
            amount_values = self.portfolio_df[amount_col].fillna(0).values
            income_std = np.std(income_values)
            amount_std = np.std(amount_values)
            if len(income_values) > 1 and income_std != 0 and amount_std != 0:
                corr_matrix = np.corrcoef(income_values, amount_values)
                corr_income_amount = corr_matrix[0, 1]

        if annual_income_col is not None and "LatestNAV" in self.portfolio_df.columns:
            income_values = self.portfolio_df[annual_income_col].fillna(0).values
            avg_nav_values = self.portfolio_df["LatestNAV"].fillna(0).values
            income_std = np.std(income_values)
            nav_std = np.std(avg_nav_values)
            if len(income_values) > 1 and income_std != 0 and nav_std != 0:
                corr_matrix = np.corrcoef(income_values, avg_nav_values)
                corr_income_avg_nav = corr_matrix[0, 1]

        if amount_col is not None and "LatestNAV" in self.portfolio_df.columns:
            amount_values = self.portfolio_df[amount_col].fillna(0).values
            avg_nav_values = self.portfolio_df["LatestNAV"].fillna(0).values
            amount_std = np.std(amount_values)
            nav_std = np.std(avg_nav_values)
            if len(amount_values) > 1 and amount_std != 0 and nav_std != 0:
                corr_matrix = np.corrcoef(amount_values, avg_nav_values)
                corr_amount_avg_nav = corr_matrix[0, 1]

        print("Correlation (Income vs Investment Amount) =", corr_income_amount)
        print("Correlation (Income vs Average Daily NAV) =", corr_income_avg_nav)
        print("Correlation (Investment Amount vs Average Daily NAV) =", corr_amount_avg_nav)

    # SECTION 9 : INVESTOR ANALYSIS
    def investor_analysis(self):
        print("\nInvestor Analysis Results")

        investor_name_col = find_column_name(self.portfolio_df, ["InvestorName", "Investor Name"])
        investor_id_col = find_column_name(self.portfolio_df, ["InvestorID_inv", "InvestorID"])
        amount_col = find_column_name(self.portfolio_df, ["Amount", "Investment Amount"])
        risk_profile_col = find_column_name(self.portfolio_df, ["RiskProfile", "Risk Profile"])
        annual_income_col = find_column_name(self.portfolio_df, ["AnnualIncome", "Annual Income"])

        if investor_name_col is not None and amount_col is not None:
            investor_value = self.portfolio_df.groupby(investor_name_col)[amount_col].sum().reset_index()
            investor_value = investor_value.rename(columns={amount_col: "PortfolioValue"})
            top_20_investors = investor_value.sort_values("PortfolioValue", ascending=False).head(20)
        else:
            top_20_investors = pd.DataFrame()

        print("Top 20 Investors based on Portfolio Value:")
        print(top_20_investors)

        if amount_col is not None:
            high_investment_records = self.portfolio_df[self.portfolio_df[amount_col] > 1000000]
        else:
            high_investment_records = pd.DataFrame()

        print("\nInvestment records greater than 10 Lakhs:")
        print(high_investment_records)

        if risk_profile_col is not None:
            high_risk_investors = self.portfolio_df[self.portfolio_df[risk_profile_col] == "High"]
        else:
            high_risk_investors = pd.DataFrame()

        print("\nHigh Risk Investors:")
        print(high_risk_investors)

        if investor_id_col is not None:
            transaction_count = self.portfolio_df.groupby(investor_id_col).size().reset_index(name="TransactionCount")
            more_than_10_transactions = transaction_count[transaction_count["TransactionCount"] > 10]
        else:
            more_than_10_transactions = pd.DataFrame()

        print("\nInvestors with more than 10 Transactions:")
        print(more_than_10_transactions)

        if annual_income_col is not None:
            income_above_15_lakhs = self.portfolio_df[self.portfolio_df[annual_income_col] > 1500000]
        else:
            income_above_15_lakhs = pd.DataFrame()

        print("\nInvestors with Annual Income greater than 15 Lakhs:")
        print(income_above_15_lakhs)

        self.investor_analysis_results["top_20_investors"] = top_20_investors
        self.investor_analysis_results["high_investment_records"] = high_investment_records
        self.investor_analysis_results["high_risk_investors"] = high_risk_investors
        self.investor_analysis_results["more_than_10_transactions"] = more_than_10_transactions
        self.investor_analysis_results["income_above_15_lakhs"] = income_above_15_lakhs

    # SECTION 10 : FUND ANALYSIS
    def fund_analysis(self):
        print("\nFund Analysis Results")

        nav_key_to_name = {}
        if "FundKey" in self.funds.columns and "FundName" in self.funds.columns:
            fund_map_df = self.funds[["FundKey", "FundName"]].dropna(subset=["FundKey", "FundName"])
            nav_key_to_name = dict(zip(fund_map_df["FundKey"], fund_map_df["FundName"]))

        nav_by_fund = self.nav_history.copy()
        nav_by_fund = nav_by_fund.sort_values(["FundKey", "Date"])

        fund_return_rows = []
        unique_fund_keys = nav_by_fund["FundKey"].dropna().unique()

        for one_fund_key in unique_fund_keys:
            one_fund_data = nav_by_fund[nav_by_fund["FundKey"] == one_fund_key]
            if len(one_fund_data) > 1:
                first_nav = one_fund_data.iloc[0]["NAV"]
                last_nav = one_fund_data.iloc[-1]["NAV"]

                if first_nav != 0:
                    return_percent = ((last_nav - first_nav) / first_nav) * 100
                else:
                    return_percent = 0

                fund_return_rows.append({
                    "FundKey": one_fund_key,
                    "ReturnPercent": return_percent
                })

        fund_returns_df = pd.DataFrame(fund_return_rows)

        if len(fund_returns_df) > 0:
            fund_returns_df["FundName"] = fund_returns_df["FundKey"].map(nav_key_to_name)

        if len(fund_returns_df) > 0:
            best_fund_row = fund_returns_df.sort_values("ReturnPercent", ascending=False).head(1)
            worst_fund_row = fund_returns_df.sort_values("ReturnPercent", ascending=True).head(1)
        else:
            best_fund_row = pd.DataFrame()
            worst_fund_row = pd.DataFrame()

        print("Best Performing Fund:")
        print(best_fund_row)

        print("\nWorst Performing Fund:")
        print(worst_fund_row)

        if "ExpenseRatio" in self.funds.columns:
            highest_expense_row = self.funds.sort_values("ExpenseRatio", ascending=False).head(1)
        else:
            highest_expense_row = pd.DataFrame()

        print("\nHighest Expense Ratio Fund:")
        print(highest_expense_row)

        if "Amount" in self.transactions.columns and "FundKey" in self.transactions.columns:
            aum_df = self.transactions.groupby("FundKey")["Amount"].sum().reset_index()
            aum_df = aum_df.rename(columns={"Amount": "AUM"})
            aum_df["FundName"] = aum_df["FundKey"].map(nav_key_to_name)
            highest_aum_row = aum_df.sort_values("AUM", ascending=False).head(1)
        else:
            aum_df = pd.DataFrame()
            highest_aum_row = pd.DataFrame()

        print("\nHighest AUM Fund:")
        print(highest_aum_row)

        if "FundKey" in self.transactions.columns:
            popularity_df = self.transactions.groupby("FundKey").size().reset_index(name="TransactionCount")
            popularity_df["FundName"] = popularity_df["FundKey"].map(nav_key_to_name)
            most_popular_fund_row = popularity_df.sort_values("TransactionCount", ascending=False).head(1)
        else:
            popularity_df = pd.DataFrame()
            most_popular_fund_row = pd.DataFrame()

        print("\nMost Popular Fund:")
        print(most_popular_fund_row)

        self.fund_analysis_results["fund_returns_df"] = fund_returns_df
        self.fund_analysis_results["best_fund_row"] = best_fund_row
        self.fund_analysis_results["worst_fund_row"] = worst_fund_row
        self.fund_analysis_results["highest_expense_row"] = highest_expense_row
        self.fund_analysis_results["aum_df"] = aum_df
        self.fund_analysis_results["highest_aum_row"] = highest_aum_row
        self.fund_analysis_results["popularity_df"] = popularity_df
        self.fund_analysis_results["most_popular_fund_row"] = most_popular_fund_row

    # SECTION 11 : OUTLIER DETECTION
    def remove_outliers(self):

        if "Amount" in self.portfolio_df.columns:
            amount_99_percentile = self.portfolio_df["Amount"].quantile(0.99)
            self.portfolio_df = self.portfolio_df[self.portfolio_df["Amount"] <= amount_99_percentile]

        nav_copy = self.nav_history.copy()
        nav_copy = nav_copy.sort_values(["FundKey", "Date"])

        nav_copy["NAVChange"] = nav_copy.groupby("FundKey")["NAV"].diff()

        nav_change_mean = nav_copy["NAVChange"].mean()
        nav_change_std = nav_copy["NAVChange"].std()

        lower_bound = nav_change_mean - (3 * nav_change_std)
        upper_bound = nav_change_mean + (3 * nav_change_std)

        nav_filtered = nav_copy[
            (nav_copy["NAVChange"].isna()) |
            ((nav_copy["NAVChange"] >= lower_bound) & (nav_copy["NAVChange"] <= upper_bound))
        ]

        self.nav_history = nav_filtered.drop(columns=["NAVChange"])

        print("Outlier removal completed.")

    # SECTION 12 : FINANCE METRICS
    def portfolio_metrics(self):
        print("\nPortfolio Metrics")

        if "Amount" not in self.portfolio_df.columns:
            self.portfolio_df["Amount"] = 0

        if "Units" not in self.portfolio_df.columns:
            self.portfolio_df["Units"] = 0

        if "LatestNAV" not in self.portfolio_df.columns:
            self.portfolio_df["LatestNAV"] = self.portfolio_df.get("NAV", 0)

        self.portfolio_df["CurrentValue"] = self.portfolio_df["Units"] * self.portfolio_df["LatestNAV"]

        total_portfolio_value = self.portfolio_df["CurrentValue"].sum()

        total_invested_amount = self.portfolio_df["Amount"].sum()

        if total_invested_amount != 0:
            portfolio_return_percent = (
                (total_portfolio_value - total_invested_amount) /
                total_invested_amount
            ) * 100
        else:
            portfolio_return_percent = 0

        absolute_return = total_portfolio_value - total_invested_amount

        if "TransactionDate" in self.portfolio_df.columns:
            min_date = self.portfolio_df["TransactionDate"].min()
            max_date = self.portfolio_df["TransactionDate"].max()

            if pd.notna(min_date) and pd.notna(max_date):
                days_difference = (max_date - min_date).days
                years_difference = days_difference / 365.0
            else:
                years_difference = 1
        else:
            years_difference = 1

        if years_difference <= 0:
            years_difference = 1

        annualized_return = portfolio_return_percent / years_difference

        if total_invested_amount > 0:
            cagr = (total_portfolio_value / total_invested_amount) ** (1 / years_difference) - 1
        else:
            cagr = 0

        unique_funds_invested = self.portfolio_df["FundKey"].nunique()
        total_funds_available = self.funds["FundKey"].nunique()

        if total_funds_available != 0:
            diversification_score = (unique_funds_invested / total_funds_available) * 100
        else:
            diversification_score = 0

        today_date = pd.to_datetime(datetime.today().date())
        if "TransactionDate" in self.portfolio_df.columns:
            self.portfolio_df["HoldingDays"] = (
                today_date - self.portfolio_df["TransactionDate"]
            ).dt.days
            average_holding_period = self.portfolio_df["HoldingDays"].mean()
        else:
            average_holding_period = 0

        if "ExpenseRatio" in self.portfolio_df.columns and total_invested_amount != 0:
            weighted_expense = self.portfolio_df["Amount"] * self.portfolio_df["ExpenseRatio"]
            expense_ratio_impact = weighted_expense.sum() / total_invested_amount
        else:
            expense_ratio_impact = 0

        risk_free_rate_percent = 5

        nav_returns = []
        nav_temp = self.nav_history.copy()
        nav_temp = nav_temp.sort_values(["FundKey", "Date"])

        fund_keys = nav_temp["FundKey"].dropna().unique()
        for one_key in fund_keys:
            one_key_data = nav_temp[nav_temp["FundKey"] == one_key]
            one_key_data = one_key_data.copy()
            one_key_data["DailyReturn"] = one_key_data["NAV"].pct_change()

            for return_value in one_key_data["DailyReturn"].dropna().values:
                nav_returns.append(return_value)

        if len(nav_returns) > 1:
            volatility = np.std(np.array(nav_returns)) * 100
        else:
            volatility = 0

        if volatility != 0:
            sharpe_ratio = (portfolio_return_percent - risk_free_rate_percent) / volatility
        else:
            sharpe_ratio = 0

        if "Category" in self.portfolio_df.columns and total_invested_amount != 0:
            category_investment = self.portfolio_df.groupby("Category")["Amount"].sum().reset_index()
            category_investment["CategoryInvestmentPercent"] = (
                category_investment["Amount"] / total_invested_amount
            ) * 100
        else:
            category_investment = pd.DataFrame()

        if "FundName" in self.portfolio_df.columns and total_invested_amount != 0:
            fund_allocation = self.portfolio_df.groupby("FundName")["Amount"].sum().reset_index()
            fund_allocation["FundAllocationPercent"] = (
                fund_allocation["Amount"] / total_invested_amount
            ) * 100
        else:
            fund_allocation = pd.DataFrame()

        if "InvestorName" in self.portfolio_df.columns:
            investor_profit_loss = self.portfolio_df.groupby("InvestorName")[["Amount", "CurrentValue"]].sum().reset_index()
            investor_profit_loss["ProfitOrLoss"] = (
                investor_profit_loss["CurrentValue"] - investor_profit_loss["Amount"]
            )
        else:
            investor_profit_loss = pd.DataFrame()

        print("Total Portfolio Value =", total_portfolio_value)
        print("Portfolio Return % =", portfolio_return_percent)
        print("Absolute Return =", absolute_return)
        print("Annualized Return =", annualized_return)
        print("CAGR =", cagr)
        print("Portfolio Diversification Score =", diversification_score)
        print("Average Holding Period (Days) =", average_holding_period)
        print("Expense Ratio Impact =", expense_ratio_impact)
        print("Sharpe Ratio (Simplified) =", sharpe_ratio)

        print("\nCategory Wise Investment %")
        print(category_investment)

        print("\nFund Allocation %")
        print(fund_allocation)

        print("\nInvestor Wise Profit/Loss")
        print(investor_profit_loss)

        self.metrics_results["total_portfolio_value"] = total_portfolio_value
        self.metrics_results["portfolio_return_percent"] = portfolio_return_percent
        self.metrics_results["absolute_return"] = absolute_return
        self.metrics_results["annualized_return"] = annualized_return
        self.metrics_results["cagr"] = cagr
        self.metrics_results["diversification_score"] = diversification_score
        self.metrics_results["average_holding_period"] = average_holding_period
        self.metrics_results["expense_ratio_impact"] = expense_ratio_impact
        self.metrics_results["sharpe_ratio"] = sharpe_ratio
        self.metrics_results["category_investment"] = category_investment
        self.metrics_results["fund_allocation"] = fund_allocation
        self.metrics_results["investor_profit_loss"] = investor_profit_loss

    # SECTION 13 : DATA VISUALIZATION
    def create_charts(self):

        chart_df = self.portfolio_df.copy()

        if "Category" in chart_df.columns and "Amount" in chart_df.columns:
            category_sum = chart_df.groupby("Category")["Amount"].sum()
            if len(category_sum) > 0 and category_sum.sum() > 0:
                plt.figure(figsize=(8, 6))
                plt.pie(category_sum, labels=category_sum.index, autopct="%1.1f%%")
                plt.title("Portfolio Allocation")
                plt.xlabel("Category")
                plt.ylabel("Allocation %")
                plt.tight_layout()
                plt.savefig(os.path.join(CHARTS_DIR, "portfolio_allocation_pie.png"))
                plt.show()

        if "FundName" in chart_df.columns and "Amount" in chart_df.columns:
            fund_sum = chart_df.groupby("FundName")["Amount"].sum().reset_index()
            if len(fund_sum) > 0:
                plt.figure(figsize=(10, 6))
                plt.bar(fund_sum["FundName"], fund_sum["Amount"])
                plt.title("Fund Wise Investment")
                plt.xlabel("Fund Name")
                plt.ylabel("Investment Amount")
                plt.xticks(rotation=45, ha="right")
                plt.tight_layout()
                plt.savefig(os.path.join(CHARTS_DIR, "fund_wise_investment_bar.png"))
                plt.show()

        if "TransactionDate" in chart_df.columns and "Amount" in chart_df.columns:
            chart_df["Month"] = chart_df["TransactionDate"].dt.to_period("M").astype(str)
            month_sum = chart_df.groupby("Month")["Amount"].sum().reset_index()
            if len(month_sum) > 0:
                plt.figure(figsize=(10, 6))
                plt.plot(month_sum["Month"], month_sum["Amount"], marker="o")
                plt.title("Monthly Investment Trend")
                plt.xlabel("Month")
                plt.ylabel("Investment Amount")
                plt.xticks(rotation=45, ha="right")
                plt.tight_layout()
                plt.savefig(os.path.join(CHARTS_DIR, "monthly_investment_trend_line.png"))
                plt.show()

        category_return_rows = []
        if "Category" in self.funds.columns and "FundKey" in self.funds.columns:
            nav_temp = self.nav_history.copy()
            nav_temp = nav_temp.sort_values(["FundKey", "Date"])
            fund_keys = nav_temp["FundKey"].dropna().unique()

            for fund_key in fund_keys:
                one_fund_nav = nav_temp[nav_temp["FundKey"] == fund_key]
                if len(one_fund_nav) > 1:
                    first_nav = one_fund_nav.iloc[0]["NAV"]
                    last_nav = one_fund_nav.iloc[-1]["NAV"]
                    if first_nav != 0:
                        one_return = ((last_nav - first_nav) / first_nav) * 100
                    else:
                        one_return = 0

                    category_value = "Unknown"
                    category_data = self.funds[self.funds["FundKey"] == fund_key]
                    if len(category_data) > 0:
                        category_value = category_data.iloc[0]["Category"]

                    category_return_rows.append({
                        "Category": category_value,
                        "ReturnPercent": one_return
                    })

        if len(category_return_rows) > 0:
            category_returns_df = pd.DataFrame(category_return_rows)
            category_returns_avg = category_returns_df.groupby("Category")["ReturnPercent"].mean().reset_index()

            plt.figure(figsize=(10, 6))
            plt.bar(category_returns_avg["Category"], category_returns_avg["ReturnPercent"])
            plt.title("Category Wise Returns")
            plt.xlabel("Category")
            plt.ylabel("Average Return %")
            plt.xticks(rotation=45, ha="right")
            plt.tight_layout()
            plt.savefig(os.path.join(CHARTS_DIR, "category_wise_returns_bar.png"))
            plt.show()

        if "Date" in self.nav_history.columns and "NAV" in self.nav_history.columns:
            nav_plot = self.nav_history.copy()
            nav_plot = nav_plot.sort_values("Date")
            if len(nav_plot) > 0:
                plt.figure(figsize=(10, 6))
                plt.plot(nav_plot["Date"], nav_plot["NAV"])
                plt.title("NAV Movement")
                plt.xlabel("Date")
                plt.ylabel("NAV")
                plt.tight_layout()
                plt.savefig(os.path.join(CHARTS_DIR, "nav_movement_line.png"))
                plt.show()

        if "InvestorName" in chart_df.columns and "Amount" in chart_df.columns:
            top_investors = chart_df.groupby("InvestorName")["Amount"].sum().reset_index()
            top_investors = top_investors.sort_values("Amount", ascending=False).head(10)
            if len(top_investors) > 0:
                plt.figure(figsize=(10, 6))
                plt.barh(top_investors["InvestorName"], top_investors["Amount"])
                plt.title("Top 10 Investors")
                plt.xlabel("Investment Amount")
                plt.ylabel("Investor Name")
                plt.tight_layout()
                plt.savefig(os.path.join(CHARTS_DIR, "top_10_investors_barh.png"))
                plt.show()

        logging.info("Charts Generated")
        print("Charts generated and saved.")

    # SECTION 14 : EXPORT REPORTS
    def export_reports(self):

        portfolio_report_data = {
            "TotalPortfolioValue": [self.metrics_results.get("total_portfolio_value", 0)],
            "PortfolioReturnPercent": [self.metrics_results.get("portfolio_return_percent", 0)],
            "AbsoluteReturn": [self.metrics_results.get("absolute_return", 0)],
            "AnnualizedReturn": [self.metrics_results.get("annualized_return", 0)],
            "CAGR": [self.metrics_results.get("cagr", 0)],
            "DiversificationScore": [self.metrics_results.get("diversification_score", 0)],
            "AverageHoldingPeriodDays": [self.metrics_results.get("average_holding_period", 0)],
            "ExpenseRatioImpact": [self.metrics_results.get("expense_ratio_impact", 0)],
            "SharpeRatioSimplified": [self.metrics_results.get("sharpe_ratio", 0)]
        }
        self.portfolio_report_df = pd.DataFrame(portfolio_report_data)

        self.investor_report_df = self.investor_analysis_results.get("top_20_investors", pd.DataFrame())

        self.fund_report_df = self.fund_analysis_results.get("fund_returns_df", pd.DataFrame())

        self.portfolio_report_df.to_csv(os.path.join(REPORTS_DIR, "portfolio_report.csv"), index=False)
        self.investor_report_df.to_csv(os.path.join(REPORTS_DIR, "investor_report.csv"), index=False)
        self.fund_report_df.to_csv(os.path.join(REPORTS_DIR, "fund_report.csv"), index=False)

        logging.info("Reports Exported")
        print("Reports exported successfully.")


# =====================================================
# SECTION 15 : MAIN FUNCTION
# =====================================================
def main():

    portfolio = FundPortfolio(
        investors=investors_df,
        funds=funds_df,
        transactions=transactions_df,
        nav_history=nav_history_df
    )

    portfolio.clean_data()
    portfolio.merge_data()
    portfolio.numpy_analysis()
    portfolio.investor_analysis()
    portfolio.fund_analysis()
    portfolio.remove_outliers()
    portfolio.portfolio_metrics()
    portfolio.create_charts()
    portfolio.export_reports()

    logging.info("Analysis Completed")
    logging.info("Program Finished")

    print("\nMutual Fund Portfolio Analysis Completed Successfully.")


if __name__ == "__main__":
    main()
