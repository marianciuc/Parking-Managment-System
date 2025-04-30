import cv2
import matplotlib.pyplot as plt
import numpy as np
import os
import re
import requests
from ultralytics import YOLO
from paddleocr import PaddleOCR


yolo_model = YOLO("best.pt")
ocr = PaddleOCR(use_angle_cls=True, lang='en')

output_dir = "output_images"
os.makedirs(output_dir, exist_ok=True)

API_URL = "http://34.53.40.156:8222/api/v1/sessions/prepare"
API_KEY = "oI0MqPNucsZmkszTYGt7-QrCKG-jE1TvqlbRhxwaGiY="


def save_and_display_image(title, image, filename):
    """Save an image to output directory and display it."""
    path = os.path.join(output_dir, filename)
    cv2.imwrite(path, image)
    print(f"{title} saved at {path}")
  #  cv2.imshow(title, image)
   # cv2.waitKey(0)
    #cv2.destroyAllWindows()

def clean_plate_text(text):
    """ Keep only letters and numbers in the license plate and filter out extra characters """
    plate_pattern = r"[A-Z0-9]+"
    matches = re.findall(plate_pattern, text)

    if matches:
        cleaned_text = " ".join(matches)
        cleaned_text = " ".join(word for word in cleaned_text.split() if len(word) >= 2)
        return cleaned_text

    return text

def remove_blue_area_and_border(image):
    """ Remove the blue EU section and the license plate border. """
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    lower_blue = np.array([90, 50, 50])
    upper_blue = np.array([130, 255, 255])
    mask = cv2.inRange(hsv, lower_blue, upper_blue)

    contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        if w > 10 and h > 10:
            image = image[:, x + w:]
            break

    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    _, thresh = cv2.threshold(gray, 200, 255, cv2.THRESH_BINARY)

    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        if w > 50 and h > 20:
            image = image[y:y + h, x:x + w]
            break

    save_and_display_image("Plate without Blue Section", image, "plate_no_blue.jpg")
    return image

def detect_license_plate(image_path):
    """ Detect the license plate using the trained YOLOv8 model. """
    image = cv2.imread(image_path)
    save_and_display_image("Original Image", image, "original.jpg")

    results = yolo_model(image)
    license_plate_bbox = None

    for result in results:
        for box in result.boxes:
            class_id = int(box.cls[0])
            x1, y1, x2, y2 = map(int, box.xyxy[0])
            if class_id == 0:
                license_plate_bbox = (x1, y1, x2, y2)
                break

    if license_plate_bbox:
        x1, y1, x2, y2 = license_plate_bbox
        cropped_plate = image[y1:y2, x1:x2]
      #  cropped_plate = remove_blue_area_and_border(cropped_plate)
        cropped_path = os.path.join(output_dir, "cropped_plate.jpg")
        cv2.imwrite(cropped_path, cropped_plate)
        save_and_display_image("Cropped License Plate", cropped_plate, "cropped_plate.jpg")
        return cropped_plate, cropped_path

    return None, None

def recognize_text(image):
    """ Recognize text from the license plate with filtering. """
    if image is None or image.size == 0:
        print("Error: Invalid or empty image passed to OCR.")
        return "Could not recognize the license plate."

    results = ocr.ocr(image, cls=True)
    if not results or results[0] is None:
        print("Error: PaddleOCR failed to process the image.")
        return "Could not recognize the license plate."

    plate_text = " ".join([line[1][0] for result in results for line in result])
    plate_text = clean_plate_text(plate_text)
    return plate_text.strip()

def alpr_pipeline(image):
    """ ALPR процесс, принимающий OpenCV-изображение или путь к файлу. """

    # Если передано изображение, сохраняем его во временный файл
    if isinstance(image, np.ndarray):
        temp_path = "temp_image.jpg"
        cv2.imwrite(temp_path, image)
        image_path = temp_path
    else:
        image_path = image  # Если передан путь, используем его напрямую

    plate_image, cropped_path = detect_license_plate(image_path)
    if plate_image is not None:
        plate_text = recognize_text(plate_image)
        return plate_text, cropped_path
    else:
        return "License plate not found.", None

# Example usage
image_path = "pol3.jpg"
plate_number, cropped_path = alpr_pipeline(image_path)
print("Recognized license plate:", plate_number)
if cropped_path:
    print("Cropped license plate saved at:", cropped_path)
