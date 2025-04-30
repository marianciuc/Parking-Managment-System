"use client";

import { useState } from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    AreaChart,
    Area,
} from "recharts";

export default function BalancePage() {
    const [selectedMonth, setSelectedMonth] = useState<string | null>(null);

    // Пример данных для доходов и расходов
    const financialData = [
        { month: "Jan", income: 10000, expense: 7000 },
        { month: "Feb", income: 12000, expense: 8500 },
        { month: "Mar", income: 15000, expense: 10000 },
        { month: "Apr", income: 14000, expense: 9500 },
        { month: "May", income: 16000, expense: 11000 },
        { month: "Jun", income: 17000, expense: 12000 },
    ];

    // Рассчитываем баланс для каждого месяца (накопления)
    const balanceData = financialData.map((data, index): { month: string; income: number; expense: number; balance: number } => {
        // Считаем баланс накоплений как сумму остатка за каждый месяц
        const previousBalance = index > 0 ? (financialData[index - 1]?.income ?? 0) - (financialData[index - 1]?.expense ?? 0) : 0;
        return {
            ...data,
            balance: previousBalance + (data.income - data.expense),
        };
    });

    // Пример данных транзакций для каждого месяца
    const transactionsData: { [key: string]: { date: string; description: string; amount: number }[] } = {
        Jan: [
            { date: "2023-01-05", description: "Office rental", amount: -5000 },
            { date: "2023-01-10", description: "Parking revenue 1", amount: 7000 },
            { date: "2023-01-20", description: "Utilities", amount: -2000 },
        ],
        Feb: [
            { date: "2023-02-02", description: "Parking revenue 2", amount: 8000 },
            { date: "2023-02-15", description: "Employee salaries", amount: -8500 },
        ],
        // Добавьте данные для остальных месяцев
    };

    const handleBack = () => {
        setSelectedMonth(null);
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">System balance</h1>

            {!selectedMonth ? (
                <div>
                    {/* Графики */}
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                        <Card>
                            <CardHeader>
                                <CardTitle>Income vs Expenses (by months)</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <ResponsiveContainer width="100%" height={300}>
                                    <LineChart data={financialData}>
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="month" />
                                        <YAxis />
                                        <Tooltip />
                                        <Line type="monotone" dataKey="income" stroke="#4CAF50" strokeWidth={2} name="Incomes" />
                                        <Line type="monotone" dataKey="expense" stroke="#F44336" strokeWidth={2} name="Expenses" />
                                    </LineChart>
                                </ResponsiveContainer>
                            </CardContent>
                        </Card>

                        {/* Новый график: динамика накоплений */}
                        <Card>
                            <CardHeader>
                                <CardTitle>Dynamics of accumulation</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <ResponsiveContainer width="100%" height={300}>
                                    <AreaChart data={balanceData}>
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="month" />
                                        <YAxis />
                                        <Tooltip />
                                        <Area
                                            type="monotone"
                                            dataKey="balance"
                                            stroke="#8884d8"
                                            fill="#8884d8"
                                            name="Balance"
                                        />
                                    </AreaChart>
                                </ResponsiveContainer>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Таблица доходов и расходов */}
                    <Card>
                        <CardHeader>
                            <CardTitle>Details of income and expenditures</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <table className="w-full text-left border-collapse">
                                <thead>
                                <tr>
                                    <th className="border-b py-2 px-4">Month</th>
                                    <th className="border-b py-2 px-4">Incomes</th>
                                    <th className="border-b py-2 px-4">Expenses</th>
                                    <th className="border-b py-2 px-4">Net income</th>
                                </tr>
                                </thead>
                                <tbody>
                                {financialData.map((data) => (
                                    <tr
                                        key={data.month}
                                        className="cursor-pointer hover:bg-gray-100"
                                        onClick={() => setSelectedMonth(data.month)}
                                    >
                                        <td className="border-b py-2 px-4">{data.month}</td>
                                        <td className="border-b py-2 px-4 text-green-600">${data.income.toLocaleString()}</td>
                                        <td className="border-b py-2 px-4 text-red-600">${data.expense.toLocaleString()}</td>
                                        <td className="border-b py-2 px-4 font-bold">
                                            ${Math.max(0, data.income - data.expense).toLocaleString()}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </CardContent>
                    </Card>
                </div>
            ) : (
                <div>
                    {/* Детализация по транзакциям за выбранный месяц */}
                    <button
                        className="text-blue-500 hover:underline mb-4"
                        onClick={handleBack}
                    >
                        ← Назад
                    </button>
                    <h2 className="text-xl font-bold mb-4">Transactions for{selectedMonth}</h2>
                    <Card>
                        <CardHeader>
                            <CardTitle>Список транзакций</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <table className="w-full text-left border-collapse">
                                <thead>
                                <tr>
                                    <th className="border-b py-2 px-4">Date</th>
                                    <th className="border-b py-2 px-4">Description</th>
                                    <th className="border-b py-2 px-4">Amount</th>
                                </tr>
                                </thead>
                                <tbody>
                                {(transactionsData[selectedMonth] || []).map((transaction, index) => (
                                    <tr key={index}>
                                        <td className="border-b py-2 px-4">{transaction.date}</td>
                                        <td className="border-b py-2 px-4">{transaction.description}</td>
                                        <td
                                            className={`border-b py-2 px-4 font-bold ${
                                                transaction.amount > 0 ? "text-green-600" : "text-red-600"
                                            }`}
                                        >
                                            ${transaction.amount.toLocaleString()}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </CardContent>
                    </Card>
                </div>
            )}
        </div>
    );
}