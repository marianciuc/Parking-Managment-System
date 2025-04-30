import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";

export default function NotFoundPage() {
    const navigate = useNavigate();

    return (
        <div className="flex items-center justify-center h-screen w-full bg-gradient-to-br from-gray-100 to-gray-300">
            <Card className="p-8 text-center shadow-xl border border-gray-200 bg-white/70 backdrop-blur-md rounded-2xl">
                <CardContent className="flex flex-col items-center gap-4">
                    <h1 className="text-4xl font-semibold text-gray-900">Oops...</h1>
                    <p className="text-gray-600">We couldn't find what you're looking for.</p>
                    <Button onClick={() => navigate("/")} className="mt-4 px-6 py-2 text-lg font-medium">
                        Go to Main Page
                    </Button>
                </CardContent>
            </Card>
        </div>
    );
}
