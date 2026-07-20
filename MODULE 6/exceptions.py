#called as errors 
#try
#except-catch
#raise-throw
#else -normal flow of program
#finally- both scenarios, whether an error occurs or not, this block will be executed

try:
    number1=int(input("Enter first number: "))
    number2=int(input("Enter second number: "))
    result = number1 / number2
except ZeroDivisionError:
    print("Error: Division by zero is not allowed.") #if the user enters 0 as the second number, this will be executed
except ValueError:
    print("Error: Invalid input. Please enter numeric values.") #if the user enters a non-numeric value, this will be executed
else:
     print(result) #if theres no error, this will be executed
finally:
    print("Execution completed.") #this will be executed regardless of whether an error occurred or not


#-Custom Exception -> class XXX(Exception): pass
class LowBalance(Exception):
    pass #placeholder for the class, no implementation needed(empty class)

amount=300
try:
    if (amount<1000):
        raise LowBalance("Low Balance in your account") #this will throw an exception if the amount is less than 1000
except LowBalance as e:
    print(e)


'''
ZeroDivisionError - Raised when the second number is zero in a division operation.
ValueError - Raised when the user enters a non-numeric value instead of an integer.
TypeError - Raised when an operation or function is applied to an object of inappropriate type.
IndexError - Raised when trying to access an index that is out of range in a list or string.
KeyError - Raised when trying to access a key that does not exist in a dictionary.
FileNotFoundError - Raised when trying to open a file that does not exist.
NameError - Raised when a variable or function name is not defined.
AttributeError - Raised when trying to access an attribute that does not exist for an object.
ImportError - Raised when an import statement fails to find the module or package being imported.
ModuleNotFoundError - Raised when a module or package cannot be found during import.
'''