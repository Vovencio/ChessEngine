import math

def compute_function():
    # Create an array to store precomputed results
    results = []

    # Iterate over x values from 0 to 16 (inclusive)
    for x in range(0, 9):
        # Replace this with your desired function
        result = math.pow(x/8, 0.9)
        results.append(result)

    # Print the Java array with double precision
    print("public static final double[] FUNCTION_VALUES = {")
    for value in results:
        print(f"    {value:.15f},")
    print("};")

# Call the function
compute_function()
