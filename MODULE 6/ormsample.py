from sqlalchemy import create_engine,Column,Integer,String
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import declarative_base

#Integer,String - for wrapper classes

#STEP 1: Create a database engine 
engine=create_engine("postgresql://postgres:12345@localhost:5432/northernarc") #connect to the database

#STEP 2: initialize the base object for the ORM mapping
Base=declarative_base()

#STEP 3: fetch session objects
Session=sessionmaker(bind=engine) #create a session to interact with the database
session=Session()

#STEP 4: Define Schema - create a class to represent the table in the database
class Scan(Base):
    __tablename__ = "scans"
    id = Column(Integer, primary_key=True) #primary key
    name = Column(String(50)) #name of the scan
    age = Column(Integer) #age of the scan
    department = Column(String(50)) #department of the scan


    def __repr__(self):
        return f"Scan(id={self.id}, name='{self.name}', age={self.age}, department='{self.department}')"

#STEP 5: create the table in the database if it doesn't exist
Base.metadata.create_all(engine) 
print("Table created successfully")


session.add_all([
    Scan(name="Alice", age=25, department="HR"),    
    Scan(name="Bob", age=30, department="Finance"),
    Scan(name="Charlie", age=35, department="IT"),
    Scan(name="David", age=40, department="Marketing"),
    Scan(name="Eve", age=28, department="HR"),
    Scan(name="Frank", age=32, department="Finance"),
    Scan(name="Grace", age=29, department="IT"),
    Scan(name="Hannah", age=35, department="Marketing"),
    Scan(name="Ian", age=31, department="Sales"),
    Scan(name="Jack", age=27, department="Operations")
])
session.commit()

session.add(Scan(name="John", age=30, department="IT")) #add a new record to the table
session.commit()

print("Record added successfully")

row_it=session.query(Scan).filter_by(department="IT").all() #fetch all records from the scans table where department is IT
for r in row_it:
    print(r)

row_first=session.query(Scan).filter_by(department="IT").first() #fetch the first record from the scans table where department is IT
print(row_first)
  
row=session.query(Scan).all() #fetch all records from the scans table
for r in row:
    print(r)

row_one=session.query(Scan).one() #fetch one record from the scans table
print(row_one)

row1=session.query(Scan.name).filter_by(department="IT").all() #fetch the names of all scans in the IT department
for r in row1:
    print(r)

row2=session.query(Scan).filter_by(department="IT").update({"age": 31}) #update the age of all scans in the IT department
print(f"{row2} records updated.")
session.commit()
print("Records updated successfully")

row3=session.query(Scan).filter_by(name="Jack").delete() #delete the record of the scan named Jack
print(f"{row3} records deleted.")
session.commit()

row_id4=session.query(Scan).filter_by(id=4).first()
print(row_id4)
row_id4.department="Sales" #update the department of the scan with id 4
session.commit()

#Filter --> how works -- filter_by() - filter by a specific column value, filter() - filter by a specific condition

session.close() #close the session
    


