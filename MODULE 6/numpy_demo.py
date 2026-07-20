import numpy as np
a=np.array([1,2,3,4,5])

print(a.sum()) #sum of all elements in the array
print(a.max()) #maximum element in the array
print(a.min()) #minimum element in the array
print(a.mean()) #mean of all elements in the array
print(a[0]) #accessing first element of the array
print(a[-1]) #accessing last element of the array
print(a[1:4]) #accessing elements from index 1 to 3

print('-' * 50)
# --multidimensional array
b=np.array([[1,2,3],[4,5,6],[7,8,9]])
print(b.sum()) #sum of all elements in the array
print(b.max()) #maximum element in the array
print(b.min()) #minimum element in the array
print(b.mean()) #mean of all elements in the array
print(b.ndim) #number of dimensions
print(b.shape) #shape of the array
print(b.dtype) #data type of the array elements

print('-' * 50)
#OTP GENERATION
otp=np.random.randint(1000,9999) #generate a random integer between 1000 and 9999
print(f"Your OTP is: {otp}")

random=np.random.rand(5) #generate 5 random numbers between 0 and 1
print(random)
