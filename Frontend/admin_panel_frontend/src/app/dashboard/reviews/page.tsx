"use client";

import { useState } from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import clsx from "clsx";

export default function ReviewsModerationPage() {
    // Пример данных отзывов
    const initialReviews = [
        {
            id: 1,
            author: "Иван Иванов",
            date: "2023-10-01",
            text: "Очень понравилось качество обслуживания. Всем рекомендую!",
            status: "pending", // Статусы: pending, approved, rejected
        },
        {
            id: 2,
            author: "Марина Дроздова",
            date: "2023-10-02",
            text: "Товар не оправдал ожиданий. Хочу вернуть деньги.",
            status: "pending",
        },
        {
            id: 3,
            author: "Александр Петухов",
            date: "2023-09-25",
            text: "Все отлично! Быстро и качественно. Спасибо.",
            status: "approved",
        },
        {
            id: 4,
            author: "Валерия Смирнова",
            date: "2023-09-15",
            text: "Упаковка была повреждена. Товар целый, но неприятно.",
            status: "rejected",
        },
    ];

    // Состояния для отзывов и фильтра
    const [reviews, setReviews] = useState(initialReviews);
    const [filter, setFilter] = useState<"all" | "pending" | "approved" | "rejected">("all");

    // Фильтрованные отзывы
    const filteredReviews =
        filter === "all"
            ? reviews
            : reviews.filter((review) => review.status === filter);

    // Функции модерации
    const updateReviewStatus = (id: number, newStatus: "approved" | "rejected") => {
        setReviews((prev) =>
            prev.map((review) =>
                review.id === id ? { ...review, status: newStatus } : review
            )
        );
    };

    const deleteReview = (id: number) => {
        setReviews((prev) => prev.filter((review) => review.id !== id));
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Reviews Moderation</h1>

            {/* Фильтры */}
            <div className="flex space-x-4 mb-4">
                {["all", "pending", "approved", "rejected"].map((status) => (
                    <button
                        key={status}
                        className={clsx(
                            "px-4 py-2 rounded border",
                            filter === status
                                ? "bg-blue-500 text-white border-blue-500"
                                : "bg-white text-gray-700 border-gray-300 hover:bg-gray-100"
                        )}
                        onClick={() => setFilter(status as typeof filter)}
                    >
                        {status === "all" ? "All" : status === "pending" ? "Pending" : status === "approved" ? "Approved" : "Rejected"}
                    </button>
                ))}
            </div>

            {/* Таблица с отзывами */}
            <Card>
                <CardHeader>
                    <CardTitle>Reviews list</CardTitle>
                </CardHeader>
                <CardContent>
                    {filteredReviews.length > 0 ? (
                        <table className="w-full text-left border-collapse">
                            <thead>
                            <tr>
                                <th className="border-b py-2 px-4">Author</th>
                                <th className="border-b py-2 px-4">Date</th>
                                <th className="border-b py-2 px-4">Text</th>
                                <th className="border-b py-2 px-4">Status</th>
                                <th className="border-b py-2 px-4">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filteredReviews.map((review) => (
                                <tr key={review.id}>
                                    <td className="border-b py-2 px-4">{review.author}</td>
                                    <td className="border-b py-2 px-4">{review.date}</td>
                                    <td className="border-b py-2 px-4 truncate max-w-md">
                                        {review.text}
                                    </td>
                                    <td className="border-b py-2 px-4">
                                        {review.status === "pending" && (
                                            <span className="text-yellow-500">Pending</span>
                                        )}
                                        {review.status === "approved" && (
                                            <span className="text-green-500">Approved</span>
                                        )}
                                        {review.status === "rejected" && (
                                            <span className="text-red-500">Rejected</span>
                                        )}
                                    </td>
                                    <td className="border-b py-2 px-4 space-x-2">
                                        {review.status === "pending" && (
                                            <>
                                                <button
                                                    className="px-2 py-1 text-white bg-green-500 rounded hover:bg-green-600"
                                                    onClick={() => updateReviewStatus(review.id, "approved")}
                                                >
                                                 Approve
                                                </button>
                                                <button
                                                    className="px-2 py-1 text-white bg-red-500 rounded hover:bg-red-600"
                                                    onClick={() => updateReviewStatus(review.id, "rejected")}
                                                >
                                                    Reject
                                                </button>
                                            </>
                                        )}
                                        <button
                                            className="px-2 py-1 text-white bg-gray-400 rounded hover:bg-gray-500"
                                            onClick={() => deleteReview(review.id)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p className="text-gray-700">There are no reviews to display.</p>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}