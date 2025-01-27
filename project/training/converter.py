import torch
import torch.nn as nn
import torch.nn.functional as F


# Define the ChessEvaluator model with the correct architecture
class ChessEvaluator(nn.Module):
    def __init__(self, input_size=70):  # Ensure the input size matches your dataset
        super(ChessEvaluator, self).__init__()
        self.fc1 = nn.Linear(input_size, input_size)  # Input -> 70 -> 70
        self.fc2 = nn.Linear(input_size, 64)           # 70 -> 64
        self.fc3 = nn.Linear(64, 64)                  # 64 -> 64
        self.fc4 = nn.Linear(64, 32)                  # 64 -> 32
        self.fc5 = nn.Linear(32, 32)                  # 32 -> 32
        self.fc6 = nn.Linear(32, 16)                  # 32 -> 16
        self.fc7 = nn.Linear(16, 16)                  # 16 -> 16
        self.fc8 = nn.Linear(16, 1)                   # 16 -> 1 (Output)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = F.relu(self.fc4(x))
        x = F.relu(self.fc5(x))
        x = F.relu(self.fc6(x))
        x = F.relu(self.fc7(x))
        x = self.fc8(x)  # Final layer outputs the evaluation score
        return x


def convert_model_to_torchscript():
    # Load the trained PyTorch model
    try:
        model = torch.load("2.pt", map_location=torch.device("cpu"))
        print("Loaded backup model.")
    except FileNotFoundError:
        print("Error: Model file '2.pt' not found!")
        return

    # Ensure the model is in evaluation mode
    model.eval()

    # Create example input matching the model's input size (70 features)
    example_input = torch.rand(1, 70)  # Batch size of 1 with 70 features

    # Convert the model to TorchScript using tracing
    scripted_model = torch.jit.trace(model, example_input)
    print("Successfully converted the model to TorchScript.")

    # Save the TorchScript model to a file
    scripted_model.save("chess_model.pt")
    print("TorchScript model saved as 'chess_model.pt'.")


if __name__ == "__main__":
    convert_model_to_torchscript()
