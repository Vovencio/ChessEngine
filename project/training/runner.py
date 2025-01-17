import sys
import json
import torch
import torch.nn as nn
import torch.nn.functional as F


class ChessEvaluator(nn.Module):
    def __init__(self, input_size=64):
        super(ChessEvaluator, self).__init__()
        self.fc1 = nn.Linear(input_size, input_size)
        self.fc2 = nn.Linear(input_size, 64)
        self.fc3 = nn.Linear(64, 64)
        self.fc4 = nn.Linear(64, 32)
        self.fc5 = nn.Linear(32, 32)
        self.fc6 = nn.Linear(32, 16)
        self.fc7 = nn.Linear(16, 16)
        self.fc8 = nn.Linear(16, 1)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = F.relu(self.fc4(x))
        x = F.relu(self.fc5(x))
        x = F.relu(self.fc6(x))
        x = F.relu(self.fc7(x))
        x = self.fc8(x)
        return x


def load_model(model_path: str):
    try:
        model = torch.load(model_path)
        model.eval()  # Set the model to evaluation mode
        print("Model loaded and ready for evaluation.", file=sys.stderr)
        return model
    except FileNotFoundError:
        print(f"Model file '{model_path}' not found. Ensure the file exists.", file=sys.stderr)
        raise
    except Exception as e:
        print(f"Error loading model: {e}", file=sys.stderr)
        raise


def get_prediction(input_data: list[int], model):
    if len(input_data) != 70:
        raise ValueError("Input must be a list of 70 integers.")
    
    input_tensor = torch.tensor(input_data, dtype=torch.float).unsqueeze(0)  # Add batch dimension
    with torch.no_grad():  # Don't track gradients during inference
        output = model(input_tensor).item()  # Convert to scalar float
    return output


if __name__ == "__main__":
    model_path = r"training\model.pt"
    model = load_model(model_path)

    # Notify Java that Python is ready
    print("Python AI is ready for evaluation.")
    sys.stdout.flush()  # Ensure the message is sent immediately

    while True:
        try:
            # Read input from Java
            input_line = sys.stdin.readline().strip()
            if not input_line:
                continue  # Wait for the next command
            
            # Parse the command and input
            command = json.loads(input_line)

            if command["type"] == "predict":
                # Run a prediction with the given data
                input_data = command["data"]
                prediction = get_prediction(input_data, model)
                response = {"status": "ok", "result": prediction}
            
            elif command["type"] == "ping":
                # Respond to a ping command
                response = {"status": "ok", "message": "Python AI is alive!"}

            elif command["type"] == "exit":
                # Exit the loop
                response = {"status": "ok", "message": "Shutting down."}
                print(json.dumps(response))
                sys.stdout.flush()
                break

            else:
                # Unknown command
                response = {"status": "error", "message": "Unknown command."}

            # Send the response to Java
            print(json.dumps(response))
            sys.stdout.flush()  # Ensure the response is sent immediately

        except Exception as e:
            # Send error details to Java
            response = {"status": "error", "message": str(e)}
            print(json.dumps(response))
            sys.stdout.flush()  # Ensure the error message is sent immediately

