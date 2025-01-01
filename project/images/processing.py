import sys
from PIL import Image
import numpy as np

def process_image(input_path, output_path, factor):
    # Open the image
    img = Image.open(input_path).convert("RGB")
    rgb_array = np.array(img, dtype=np.float64)  # Use float64 for precision
    
    # Calculate the average of RGB values (luminance)
    luminance_array = np.sum(rgb_array, axis=2) / 3  # Average RGB values

    # Normalize and scale
    luminance_array = (luminance_array / 255) * factor  # Scale to the desired factor

    # Write the Java static final array
    with open(output_path, 'w') as file:
        file.write("public static final double[][] LUMINANCE_VALUES = {\n")
        for row in luminance_array:
            row_data = ", ".join([f"{val:.10f}" for val in row])  # 10 decimal places
            file.write(f"    {{{row_data}}},\n")
        file.write("};\n")

def main():
    if len(sys.argv) != 4:
        print("Usage: python processing.py <input_image> <output_file> <factor>")
        sys.exit(1)

    input_path = sys.argv[1]
    output_path = sys.argv[2]
    factor = float(sys.argv[3])

    process_image(input_path, output_path, factor)

if __name__ == "__main__":
    main()
