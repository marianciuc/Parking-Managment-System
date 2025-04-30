import io
import numpy as np
import cv2
from PIL import Image


class CameraService:
    def __init__(self):
        # Найти доступную камеру
        self.camera_index = 0
        self.video_capture = None
        self.is_preview_running = False

    def _ensure_camera_initialized(self):
        """Инициализирует камеру, если она еще не инициализирована"""
        if self.video_capture is None:
            self.video_capture = cv2.VideoCapture(self.camera_index)
            if not self.video_capture.isOpened():
                raise RuntimeError("Не удалось открыть камеру. Проверьте подключение.")
            self.video_capture.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
            self.video_capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)


    def start_preview(self, window_name="Camera Preview"):
        """Запускает предварительный просмотр с камеры в отдельном окне."""
        self._ensure_camera_initialized()
        self.is_preview_running = True

        try:
            while self.is_preview_running:
                ret, frame = self.video_capture.read()
                if not ret:
                    break

                # Отображение кадра
                cv2.imshow(window_name, frame)

                # Проверка нажатия клавиши ESC для выхода
                if cv2.waitKey(1) & 0xFF == 27:  # ESC key
                    break
        finally:
            cv2.destroyAllWindows()
            if self.is_preview_running:
                self.stop_preview()

    def stop_preview(self):
        """Останавливает предварительный просмотр."""
        self.is_preview_running = False
        if self.video_capture is not None and self.video_capture.isOpened():
            self.video_capture.release()
            self.video_capture = None
        cv2.destroyAllWindows()

    def capture_image(self, show_preview=False):
        """Захватывает изображение с камеры."""
        was_running = self.is_preview_running

        if not self.is_preview_running:
            self._ensure_camera_initialized()

        # Сделаем несколько захватов для стабилизации автофокуса
        for _ in range(5):
            ret, frame = self.video_capture.read()
            if not ret:
                raise RuntimeError("Не удалось получить кадр с камеры")

        if show_preview:
            cv2.imshow("Captured Image", frame)
            cv2.waitKey(2000)
            cv2.destroyWindow("Captured Image")

        if not was_running:
            self.video_capture.release()
            self.video_capture = None

        # Преобразуем BGR (OpenCV) в RGB
        image_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        return image_rgb

    def __del__(self):
        """Деструктор для освобождения ресурсов камеры"""
        if self.video_capture is not None and self.video_capture.isOpened():
            self.video_capture.release()
