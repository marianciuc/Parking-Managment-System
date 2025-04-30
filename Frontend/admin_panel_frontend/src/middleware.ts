import {NextResponse, NextRequest} from 'next/server';
import jwt from 'jsonwebtoken';
import {AuthResponse} from "@/interfaces";

const BASE_URL = "https://marianciuc.works";

async function isTokenExpired(token: string): Promise<boolean> {
    try {
        const decodedToken: { exp?: number } = jwt.verify(token, await getPublicJwtKey()) as { exp?: number };
        if (!decodedToken?.exp) {
            return true;
        }
        const currentTime = Math.floor(Date.now() / 1000);
        return decodedToken.exp < currentTime;
    } catch {
        return true;
    }
}

async function getPublicJwtKey() {
    const response = await fetch(`${BASE_URL}/api/v1/security/jwt`);
    return await response.text();
}

async function refreshAccessToken(refreshToken: string): Promise<AuthResponse | null> {
    try {
        const response = await fetch(`${BASE_URL}/api/v1/security/jwt/refresh`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${refreshToken}`
            },
        });
        return response.ok ? await response.json() as AuthResponse : null;
    } catch (e) {
        console.error('Error checking permissions:', e);
        return null;
    }
}

async function hasPermissions(accessToken: string): Promise<boolean> {
    try {
        const response = await fetch(`${BASE_URL}/api/v1/administrators/permissions`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
        });
        return response.ok;
    } catch (e) {
        console.error('Error checking permissions:', e);
        return false;
    }
}


export async function middleware(req: NextRequest) {
    console.log("Middleware triggered for path:", req.nextUrl.pathname);

    const accessToken = req.cookies.get('accessToken')?.value;
    const refreshToken = req.cookies.get('refreshToken')?.value;

    console.log('Checking permissions...');

    if (!accessToken || !refreshToken) {
        return NextResponse.redirect(new URL('/login', req.url));
    }

    if (await isTokenExpired(accessToken)) {
        console.log('Access token expired. Attempting to refresh...');
        const tokenPair = await refreshAccessToken(refreshToken);

        if (tokenPair != null) {
            const isAuthorized = accessToken ? await hasPermissions(accessToken) : false;

            if (!isAuthorized) {
                return NextResponse.redirect(new URL('/403', req.url));
            }

            const response = NextResponse.next();
            response.cookies.set('accessToken', tokenPair.accessToken, {
                httpOnly: true,
                secure: process.env.NODE_ENV === 'production',
                sameSite: 'strict',
                path: '/',
            });
            response.cookies.set('refreshToken', tokenPair.refreshToken)
            return response;
        }

    }
    const isAuthorized = accessToken ? await hasPermissions(accessToken) : false;

    if (!isAuthorized) {
        return NextResponse.redirect(new URL('/login', req.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/', '/dashboard/:path*'],
};