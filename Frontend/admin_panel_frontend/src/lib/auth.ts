const API_URL = 'https://example.com/api'; // Сторонний сервер

// Функция для логина: запрашивает `access` и `refresh` токены
export async function login(email: string, password: string) {
    const res = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
        const error = await res.json();
        throw new Error(error.message || 'Login failed');
    }

    const data = await res.json();

    // Сохраняем токены в `localStorage` (или с помощью cookies)
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);

    return data;
}

// Функция для запроса обновления `accessToken` с `refreshToken`
export async function refreshAccessToken() {
    const refreshToken = localStorage.getItem('refreshToken');

    if (!refreshToken) {
        throw new Error('Refresh token missing');
    }

    const res = await fetch(`${API_URL}/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken }),
    });

    if (!res.ok) {
        throw new Error('Failed to refresh access token');
    }

    const data = await res.json();

    // Обновляем `accessToken`
    localStorage.setItem('accessToken', data.accessToken);

    return data.accessToken;
}

// Выход пользователя: чистим токены
export function logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
}