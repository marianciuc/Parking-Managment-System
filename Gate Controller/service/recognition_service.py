import re
import os
import tempfile
import shutil
import uuid
from io import BytesIO
from pathlib import Path
from ultralytics import YOLO
import easyocr
import cv2
import numpy as np
import matplotlib.pyplot as plt


class RecognitionService:

    def __init__(self):
        self.yolo_model = YOLO("best.pt")
        self.ocr = easyocr.Reader(['en'], gpu=True)
        self.debug_dir = None
        self.debug_images_paths = {}

    def _initialize_debug_directory(self):
        """Создает временную директорию для хранения отладочных изображений"""
        if self.debug_dir is None:
            self.debug_dir = tempfile.mkdtemp(prefix="recognition_debug_")
        return self.debug_dir

    def _cleanup_debug_directory(self):
        """Удаляет временную директорию и все файлы в ней"""
        if self.debug_dir and os.path.exists(self.debug_dir):
            shutil.rmtree(self.debug_dir)
            self.debug_dir = None
            self.debug_images_paths.clear()

    def recognize(self, image):
        try:
            # Создаем временную директорию и очищаем предыдущие данные
            self._initialize_debug_directory()
            self.debug_images_paths.clear()

            if isinstance(image, bytes):
                image = np.frombuffer(image, np.uint8)
                img = cv2.imdecode(image, cv2.IMREAD_COLOR)
            elif isinstance(image, np.ndarray):
                img = image.copy()
            else:
                raise ValueError("Неподдерживаемый тип изображения. Ожидается np.ndarray или bytes")

            self._store_debug_image("original", img, "Исходное изображение")

            plate_image = self._detect_license_plate(img)
            if plate_image is not None:
                processed_plate = self._remove_blue_area_and_border(plate_image)
                self._store_debug_image("processed_plate", processed_plate, "Обработанный номерной знак")

                plate_text = self._recognize_text(processed_plate)
                return plate_text, processed_plate
            else:
                raise ValueError("Номерной знак не обнаружен")
        finally:
            # Очистка будет происходить только если пользователь явно не запросил
            # отладочные изображения через get_debug_image* методы
            pass

    def _recognize_text(self, image):
        if image is None or image.size == 0:
            print("Ошибка: Недопустимое или пустое изображение передано в OCR")
            return "Не удалось распознать номерной знак"

        preprocessed = self._preprocess_for_ocr(image)
        self._store_debug_image("ocr_preprocessing", preprocessed, "Предобработка для OCR")

        # EasyOCR работает как с BGR, так и с RGB изображениями
        results = self.ocr.readtext(preprocessed)
        if not results:
            # Пробуем распознать оригинальное изображение, если предобработанное не дало результатов
            results = self.ocr.readtext(image)
            if not results:
                print("Ошибка: EasyOCR не смог обработать изображение")
                return "Не удалось распознать номерной знак"

        # Собираем текст с учетом уверенности распознавания
        high_conf_texts = []
        for result in results:
            box, text, confidence = result
            if confidence > 0.3:  # Фильтруем тексты с низкой уверенностью
                high_conf_texts.append(text)

        plate_text = " ".join(high_conf_texts) if high_conf_texts else ""
        plate_text = self._clean_plate_text(plate_text)

        # Сохраняем визуализацию результатов OCR
        vis_image = image.copy()
        for result in results:
            box = result[0]  # EasyOCR возвращает 4 точки для box
            text = result[1]
            confidence = result[2]

            # Преобразуем точки в формат, понятный OpenCV
            box = np.array(box).astype(np.int32).reshape((-1, 1, 2))
            cv2.polylines(vis_image, [box], True, (0, 255, 0), 2)

            # Используем первую точку бокса для размещения текста
            cv2.putText(vis_image, f"{text} ({confidence:.2f})",
                        (int(box[0][0][0]), int(box[0][0][1]) - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

        self._store_debug_image("ocr_result", vis_image, "Результат OCR распознавания")

        return plate_text.strip()

    def _preprocess_for_ocr(self, image):
        """Улучшает изображение для более точного OCR распознавания."""
        # Конвертируем в оттенки серого
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        # Применяем нормализацию гистограммы для улучшения контраста
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
        equalized = clahe.apply(gray)

        # Применяем фильтр Гаусса для удаления шума
        blurred = cv2.GaussianBlur(equalized, (5, 5), 0)

        # Адаптивная бинаризация
        thresh = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                       cv2.THRESH_BINARY, 11, 2)

        # Морфологические операции для удаления мелких шумов
        kernel = np.ones((1, 1), np.uint8)
        opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel)

        # Возвращаем обработанное изображение в цветном формате для совместимости с OCR
        return cv2.cvtColor(opening, cv2.COLOR_GRAY2BGR)

    def _detect_license_plate(self, image):
        """Обнаруживает номерной знак с помощью обученной модели YOLOv8."""
        results = self.yolo_model(image)

        # Создаем копию для визуализации
        detection_vis = image.copy()

        for result in results:
            for box in result.boxes:
                class_id = int(box.cls[0])
                confidence = float(box.conf[0])
                x1, y1, x2, y2 = map(int, box.xyxy[0])

                # Рисуем все детекции для отладки
                label = f"Class {class_id}: {confidence:.2f}"
                color = (0, 255, 0) if class_id == 0 else (0, 0, 255)
                cv2.rectangle(detection_vis, (x1, y1), (x2, y2), color, 2)
                cv2.putText(detection_vis, label, (x1, y1 - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

                if class_id == 0:  # Предполагаем, что class_id 0 соответствует номерному знаку
                    cropped_plate = image[y1:y2, x1:x2]
                    self._store_debug_image("cropped_plate", cropped_plate, "Вырезанный номерной знак")
                    self._store_debug_image("detection_result", detection_vis, "Результат детекции")
                    return cropped_plate

        # Если номер не найден, все равно сохраняем визуализацию
        self._store_debug_image("detection_result", detection_vis, "Результат детекции (номер не найден)")
        return None

    def _remove_blue_area_and_border(self, image):
        """Удаляет синюю секцию ЕС и границу номерного знака."""
        if image is None:
            return None

        # Копии для отладки
        original = image.copy()

        # Выделяем синюю область
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        lower_blue = np.array([90, 50, 50])
        upper_blue = np.array([130, 255, 255])
        mask = cv2.inRange(hsv, lower_blue, upper_blue)

        # Сохраняем маску синего цвета для отладки
        mask_colored = cv2.cvtColor(mask, cv2.COLOR_GRAY2BGR)
        mask_colored[mask > 0] = [0, 0, 255]  # Красим маску в красный для наглядности
        self._store_debug_image("blue_mask", mask_colored, "Маска синей области")

        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        # Отрисовываем контуры на дебаг-изображении
        debug_contours = image.copy()
        cv2.drawContours(debug_contours, contours, -1, (0, 255, 0), 2)
        self._store_debug_image("blue_contours", debug_contours, "Контуры синей области")

        for contour in contours:
            x, y, w, h = cv2.boundingRect(contour)
            if w > 10 and h > 10 and x + w < image.shape[1]:  # Проверка на границы изображения
                image = image[:, x + w:]
                break

        self._store_debug_image("after_blue_removal", image, "После удаления синей области")

        # Пороговая обработка для поиска границ
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        _, thresh = cv2.threshold(gray, 200, 255, cv2.THRESH_BINARY)
        self._store_debug_image("threshold", cv2.cvtColor(thresh, cv2.COLOR_GRAY2BGR), "Бинаризация")

        contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        # Отрисовываем контуры на дебаг-изображении
        debug_border_contours = image.copy()
        cv2.drawContours(debug_border_contours, contours, -1, (0, 255, 0), 2)
        self._store_debug_image("border_contours", debug_border_contours, "Контуры границы")

        for contour in contours:
            x, y, w, h = cv2.boundingRect(contour)
            if w > 50 and h > 20:
                # Проверяем, что границы не выходят за пределы изображения
                y_end = min(y + h, image.shape[0])
                x_end = min(x + w, image.shape[1])
                image = image[y:y_end, x:x_end]
                break

        self._store_debug_image("plate_no_blue", image, "Номер после обработки")

        # Создаем составное изображение для демонстрации всей последовательности
        self._create_processing_pipeline_visualization(original, image)

        return image

    def _create_processing_pipeline_visualization(self, original, final):
        """Создает визуализацию всего процесса обработки для отладки."""
        if original is None or final is None:
            return

        # Получаем промежуточные результаты
        steps = [
            ("original", "Исходный номер"),
            ("blue_mask", "Маска синего"),
            ("after_blue_removal", "Без синей области"),
            ("threshold", "Бинаризация"),
            ("plate_no_blue", "Итоговый результат")
        ]

        # Собираем только доступные изображения
        available_images = []
        for key, title in steps:
            img_path = self.debug_images_paths.get(key)
            if img_path and os.path.exists(img_path):
                img = cv2.imread(img_path)
                if img is not None:
                    available_images.append((img, title))

        # Если мало изображений, не создаем визуализацию
        if len(available_images) < 2:
            return

        # Создаем визуализацию процесса
        pipeline_image = self._create_grid_visualization(available_images)
        self._store_debug_image("processing_pipeline", pipeline_image, "Процесс обработки номера")

    def _store_debug_image(self, name, image, title=""):
        """Сохраняет промежуточное изображение в файл для отладки."""
        if image is None:
            return

        # Создаем временную директорию если её ещё нет
        debug_dir = self._initialize_debug_directory()

        # Генерируем уникальное имя файла
        unique_filename = f"{name}_{uuid.uuid4().hex}.jpg"
        file_path = os.path.join(debug_dir, unique_filename)

        # Сохраняем изображение
        cv2.imwrite(file_path, image)

        # Сохраняем путь и метаданные
        self.debug_images_paths[name] = file_path

    def get_debug_image(self, name):
        """Получает отладочное изображение по имени."""
        file_path = self.debug_images_paths.get(name)
        if file_path and os.path.exists(file_path):
            return cv2.imread(file_path)
        return None

    def get_debug_image_bytes(self, name, format='.jpg'):
        """Получает отладочное изображение в виде байтов."""
        image = self.get_debug_image(name)
        if image is None:
            return None

        is_success, buffer = cv2.imencode(format, image)
        if is_success:
            return BytesIO(buffer).getvalue()
        return None

    def plot_debug_images(self, figsize=(15, 10)):
        """Отображает все отладочные изображения в виде сетки."""
        if not self.debug_images_paths:
            print("Нет отладочных изображений для отображения")
            return

        # Загружаем все изображения из файлов
        debug_images = {}
        for name, file_path in self.debug_images_paths.items():
            if os.path.exists(file_path):
                img = cv2.imread(file_path)
                if img is not None:
                    debug_images[name] = {
                        'image': img,
                        'title': name  # Используем имя как заголовок
                    }

        if not debug_images:
            print("Не удалось загрузить ни одного отладочного изображения")
            return

        # Определяем размеры сетки
        n = len(debug_images)
        cols = min(3, n)  # Максимально 3 колонки
        rows = (n + cols - 1) // cols

        # Создаем фигуру и оси
        fig, axes = plt.subplots(rows, cols, figsize=figsize)
        if rows == 1 and cols == 1:
            axes = np.array([axes])
        axes = axes.flatten()

        # Заполняем графики
        for i, (name, data) in enumerate(debug_images.items()):
            if i < len(axes):
                img = data['image']
                title = data['title']

                # Конвертируем BGR в RGB для корректного отображения
                if len(img.shape) == 3 and img.shape[2] == 3:
                    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

                axes[i].imshow(img)
                axes[i].set_title(title)
                axes[i].axis('off')

        # Скрываем пустые оси
        for i in range(len(debug_images), len(axes)):
            axes[i].axis('off')

        plt.tight_layout()
        return fig

    def get_debug_plot_as_bytes(self, figsize=(15, 10), dpi=100):
        """Получает график с отладочными изображениями в виде байтов."""
        fig = self.plot_debug_images(figsize=figsize)
        if fig is None:
            return None

        buf = BytesIO()
        fig.savefig(buf, format='png', dpi=dpi, bbox_inches='tight')
        plt.close(fig)
        buf.seek(0)
        return buf.getvalue()

    def _create_grid_visualization(self, images_with_titles, cols=3):
        """Создает сетку изображений с подписями."""
        n = len(images_with_titles)
        if n == 0:
            return None

        # Определяем размеры сетки
        cols = min(cols, n)
        rows = (n + cols - 1) // cols

        # Найдем максимальную высоту и ширину для масштабирования
        target_height = 150
        target_width = 250

        # Создаем пустое изображение для сетки
        grid_height = rows * (target_height + 30)  # Дополнительно для текста
        grid_width = cols * target_width
        grid = np.ones((grid_height, grid_width, 3), dtype=np.uint8) * 255

        # Заполняем сетку изображениями
        for i, (img, title) in enumerate(images_with_titles):
            row = i // cols
            col = i % cols

            # Масштабируем изображение
            h, w = img.shape[:2]
            scale = min(target_width / w, target_height / h)
            new_w, new_h = int(w * scale), int(h * scale)

            resized = cv2.resize(img, (new_w, new_h))

            # Определяем координаты для вставки
            x_offset = col * target_width + (target_width - new_w) // 2
            y_offset = row * (target_height + 30) + (target_height - new_h) // 2

            # Вставляем изображение
            grid[y_offset:y_offset + new_h, x_offset:x_offset + new_w] = resized

            # Добавляем подпись
            text_x = col * target_width + 5
            text_y = row * (target_height + 30) + target_height + 20
            cv2.putText(grid, title, (text_x, text_y),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1)

        return grid

    def __del__(self):
        """Деструктор для очистки всех временных файлов при уничтожении объекта"""
        self._cleanup_debug_directory()

    def cleanup(self):
        """Явный метод для очистки всех временных файлов"""
        self._cleanup_debug_directory()

    @staticmethod
    def _clean_plate_text(text):
        """Оставляет только буквы и цифры в номерном знаке и отфильтровывает лишние символы."""
        # Паттерн более гибкий для работы с результатами EasyOCR и учета возможных ошибок распознавания
        plate_pattern = r"[A-Z0-9]{2,}"
        matches = re.findall(plate_pattern, text.upper())

        if matches:
            # Фильтруем слишком короткие последовательности
            cleaned_text = " ".join(word for word in matches if len(word) >= 2)
            return cleaned_text

        return text.upper()  # Возвращаем все в верхнем регистре для номерных знаков
