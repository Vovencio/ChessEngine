import json
import torch
from torch.utils.data import DataLoader, Dataset
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import time

# Ensure training runs on CPU
print("Training will run on CPU.")
device = torch.device("cpu")

class ChessDataset(Dataset):  # Ensure it inherits from `Dataset`
    def __init__(self):
        self.loaded_data = []

        datapathv = "dataset-probs-done.jsonl"

        print(datapathv)

        with open(datapathv, "r") as f:
            for line in f:
                if line.strip():  # Skip empty lines
                    self.loaded_data.append(json.loads(line))

        self.data = []
        self.labels = []
        self.max_train_data = 694_200
        for state in self.loaded_data:
            self.max_train_data -= 1
            if self.max_train_data <= 1:
                break
            self.data.append(torch.tensor(state["data"], dtype=torch.float))
            self.labels.append(torch.tensor(state["cp"], dtype=torch.float))

        print("Loaded dataset!")

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        return [self.data[idx], self.labels[idx]]


class ChessEvaluator(nn.Module):
    def __init__(self, input_size=70):
        super(ChessEvaluator, self).__init__()
        self.fc1 = nn.Linear(input_size, 256)
        self.fc2 = nn.Linear(256, 64)
        self.fc3 = nn.Linear(64, 32)
        self.fc4 = nn.Linear(32, 32)
        self.fc5 = nn.Linear(32, 1)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = F.relu(self.fc4(x))
        x = self.fc5(x)  # No activation for the final layer
        return x


def main():
    # Model and data
    dataset = ChessDataset()
    dataloader = DataLoader(dataset, batch_size=256, shuffle=True, num_workers=0, pin_memory=False)

    try:
        model = torch.load("backup.pt")
        print("Loaded backup model.")
    except:
        print("No backup found, creating new.")
        model = ChessEvaluator(input_size=len(dataset[0][0]))

    # Training setup
    criterion = nn.MSELoss()
    optimizer = optim.Adam(model.parameters(), lr=0.001)

    epochs = 1000
    current_time = time.time()
    last_time = current_time

    save_interval = 1

    current_epoch = 0
    epoch = 0

    while True:
        epoch += 1
        current_epoch += 1
        model.train()  # Set model to training mode
        epoch_loss = 0.0  # Track total loss for the epoch
        batch_count = 0  # Count batches for averaging
        for batch_idx, (data, target) in enumerate(dataloader):
            # Keep data and target on the CPU
            data, target = data.to(device), target.to(device)
            optimizer.zero_grad()  # Reset gradients
            output = model(data)  # Forward pass
            output = output.squeeze(1)  # Converts shape from (batch_size, 1) to (batch_size)
            loss = criterion(output, target)  # Compute loss
            loss.backward()  # Backpropagation
            optimizer.step()  # Update weights
            # Accumulate loss
            epoch_loss += loss.item()
            batch_count += 1
        # Save and report progress at intervals
        if current_epoch == save_interval:
            torch.save(model, "backup.pt")
            current_epoch = 0
            avg_loss = epoch_loss / batch_count  # Compute average loss for the epoch
            print(f"Epoch {epoch}, Average Loss: {avg_loss:.4f}")
            current_time = time.time()
            print("Time: ", current_time - last_time)
            last_time = current_time


if __name__ == "__main__":
    main()
