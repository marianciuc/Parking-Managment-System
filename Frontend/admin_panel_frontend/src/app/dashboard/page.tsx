"use client";

import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    BarChart,
    Bar,
    PieChart,
    Pie,
    Cell,
    Legend,
} from "recharts";

const DashboardPage = () => {
    const stats = [
        { label: "Drivers", value: "1,245" },
        { label: "Parking lots", value: "432" },
        { label: "Income (per month)", value: "$12,450" },
    ];

    const incomeData = [
        { month: "Jan", income: 8000 },
        { month: "Feb", income: 9600 },
        { month: "Mar", income: 12000 },
        { month: "Apr", income: 15000 },
        { month: "May", income: 14000 },
        { month: "Jun", income: 16000 },
    ];

    const activityData = [
        { name: "Drivers", value: 1245 },
        { name: "Parking lots", value: 432 },
        { name: "Clients", value: 3200 },
    ];

    const trafficData = [
        { day: "Mon", visits: 300 },
        { day: "Tue", visits: 450 },
        { day: "Wed", visits: 350 },
        { day: "Thu", visits: 500 },
        { day: "Fri", visits: 700 },
        { day: "Sat", visits: 600 },
        { day: "Sun", visits: 400 },
    ];

    const pieColors = ["#0088FE", "#00C49F", "#FFBB28"];

    return (
        <div>
            <h1 className="text-2xl font-bold mb-6">System overview</h1>

            {/* Статистика */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                {stats.map((stat) => (
                    <Card key={stat.label} className="shadow-md">
                        <CardHeader>
                            <CardTitle>{stat.label}</CardTitle>
                        </CardHeader>
                        <CardContent className="text-2xl font-bold">{stat.value}</CardContent>
                    </Card>
                ))}
            </div>

            {/* Графики */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                {/* Линейный график: дохід */}
                <Card className="shadow-md col-span-1">
                    <CardHeader>
                        <CardTitle>Динамика дохода</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={incomeData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="month" />
                                <YAxis />
                                <Tooltip />
                                <Line type="monotone" dataKey="income" stroke="#8884d8" strokeWidth={2} />
                            </LineChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                {/* Круговая диаграмма: активность */}
                <Card className="shadow-md col-span-1">
                    <CardHeader>
                        <CardTitle>Activity on the platform</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <ResponsiveContainer width="100%" height={300}>
                            <PieChart>
                                <Pie
                                    data={activityData}
                                    dataKey="value"
                                    nameKey="name"
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={100}
                                    fill="#8884d8"
                                >
                                    {activityData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={pieColors[index % pieColors.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                                <Legend />
                            </PieChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                {/* Бар-чарт: посещения за неделю */}
                <Card className="shadow-md col-span-1 md:col-span-2">
                    <CardHeader>
                        <CardTitle>Weekly visits</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <ResponsiveContainer width="100%" height={300}>
                            <BarChart data={trafficData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="day" />
                                <YAxis />
                                <Tooltip />
                                <Bar dataKey="visits" fill="#82ca9d" />
                            </BarChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
};

export default DashboardPage;