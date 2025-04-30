import * as SecureStore from 'expo-secure-store';

const getRefreshToken = async () => {
    return SecureStore.getItemAsync('refreshToken');
}

const getAccessToken = async () => {
    return SecureStore.getItemAsync('accessToken');
}

const setRefreshToken = async (refreshToken: string) => {
    return  SecureStore.setItemAsync('refreshToken', refreshToken);
}

const setAccessToken = async (accessToken: string) => {
    return  SecureStore.setItemAsync('accessToken', accessToken);
}

export {getAccessToken, getRefreshToken, setAccessToken, setRefreshToken}
