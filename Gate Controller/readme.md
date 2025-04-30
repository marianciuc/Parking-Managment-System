# Parking Gate Controller

Parking Gate Controller was created with Python and provides API routes for gate operations such as opening and closing the gate. This project can only be assembled and run on a Raspberry Pi.

## Installation and Setup

Follow these steps to set up the Parking Gate Controller on your Raspberry Pi:

#### 1. Connect to Raspberry Pi

To connect, you must be on the same network as the Raspberry Pi:
```bash
ssh root@raspberrypi.local
```

#### 2. Copy files to Raspberry Pi

Transfer all project files from your local computer to the Raspberry Pi:
```bash
scp -r /path/to/local/project root@raspberrypi.local:/home/pi/parking-gate-controller
```

#### 3. Go to workspace folder

Navigate to the project directory on the Raspberry Pi:
```bash
cd /home/pi/parking-gate-controller
```

#### 4. Install Python

Ensure Python is installed on your Raspberry Pi:
```bash
sudo apt update
sudo apt install python3 python3-pip
```

#### 5. Create virtual environment

Create and activate a Python virtual environment:
```bash
python3 -m venv venv
source venv/bin/activate
```

#### 6. Install requirements

Install all required dependencies:
```bash
pip install -r requirements.txt
```

#### 7. Modify configuration file

Edit the configuration file with your specific settings:
```bash
nano config.yaml
```

See the [Configuration File](#configuration-file) section for details on available settings.


#### 8. Run application

Start the Parking Gate Controller application:
```bash
python app.py
```

The controller will now be running and listening for API requests.

## Hardware Connections

Connect the following components to the Raspberry Pi's GPIO pins:

### LED Connections
- **Red LED**: Connect to GPIO pin 19
  - Positive lead (anode) to GPIO 19 through a 220Ω resistor
  - Negative lead (cathode) to GND
- **Green LED**: Connect to GPIO pin 16
  - Positive lead (anode) to GPIO 16 through a 220Ω resistor
  - Negative lead (cathode) to GND

### Servo Motor Connection
- **Signal wire** (usually orange or yellow): Connect to GPIO pin 18 (PWM pin)
- **Power wire** (usually red): Connect to 5V pin
- **Ground wire** (usually brown or black): Connect to GND

### Sound Sensor Connection
- **VCC**: Connect to 3.3V pin
- **GND**: Connect to GND
- **Digital Output (DO)**: Connect to GPIO pin 23
- **Analog Output (AO)**: Connect to GPIO pin 24 (if using analog readings via ADC)

**Note**: Always ensure the Raspberry Pi is powered off when connecting or disconnecting hardware components.

## Setting Up Ngrok for Remote Access

To make the Parking Gate Controller accessible from the internet, you can use Ngrok to create a secure tunnel to your Raspberry Pi.

### 1. Install Ngrok

```bash
# Download Ngrok
wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-arm.tgz

# Extract the archive
tar xvzf ngrok-v3-stable-linux-arm.tgz

# Move ngrok to a directory in your PATH
sudo mv ngrok /usr/local/bin/
```

### 2. Configure Ngrok

Sign up for a free account at [ngrok.com](https://ngrok.com) and get your authtoken, then configure it:

```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN
```

### 3. Create a startup script

Create a script to run Ngrok as a service:

```bash
nano start_ngrok.sh
```

Add the following content:

```bash
#!/bin/bash
ngrok http 5000 --log=stdout > /home/pi/ngrok.log &
```

Make the script executable:

```bash
chmod +x start_ngrok.sh
```

### 4. Run Ngrok

```bash
./start_ngrok.sh
```

### 5. Get the public URL

```bash
curl http://localhost:4040/api/tunnels | jq '.tunnels[0].public_url'
```

This will display the public URL you can use to access your Parking Gate Controller.

### 6. Setup Ngrok as a service (optional)

To ensure Ngrok starts automatically when your Raspberry Pi boots:

```bash
sudo nano /etc/systemd/system/ngrok.service
```

Add the following content:

```ini
[Unit]
Description=Ngrok
After=network.service

[Service]
ExecStart=/usr/local/bin/ngrok http 5000
Restart=always
User=pi

[Install]
WantedBy=multi-user.target
```

Enable and start the service:

```bash
sudo systemctl enable ngrok.service
sudo systemctl start ngrok.service
```

## Configuration File

The `config.ini` file contains all settings for the Parking Gate Controller. Here's an explanation of the available options:

```ini
[app]
debug = true
log_level = info
backend_uri = <gateway-url>
host = https://70b6-82-145-84-221.ngrok-free.app # Your ngrok url
name = gate-east
api_key = <api-key-generated-by-system>
type = IN
port = 5331
```

Modify these settings according to your specific requirements and hardware configuration.
