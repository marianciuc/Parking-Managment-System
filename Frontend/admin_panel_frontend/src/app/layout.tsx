import "./globals.css";
import ClientContextProvider from "@/providers/ClientContextProvider";
import {Toaster} from "sonner";


export const metadata = {
    title: 'HandyParking Admin',
    description: 'Admin dashboard for HandyParking',
};

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
        <body>
        <ClientContextProvider>
            {children}
            <Toaster />
        </ClientContextProvider>
        </body>
        </html>
    );
}