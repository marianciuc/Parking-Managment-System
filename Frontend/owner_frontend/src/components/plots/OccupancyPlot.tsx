"use client"


import { CartesianGrid, Line, LineChart, XAxis } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart"
import DatePickerFromTo from "@/components/DatePickerFromTo.tsx";
import * as React from "react";


const chartData = [
    { hour: "07:00", occupancy: 22 },
    { hour: "08:00", occupancy: 45 },
    { hour: "09:00", occupancy: 68 },
    { hour: "10:00", occupancy: 80 },
    { hour: "11:00", occupancy: 92 },
    { hour: "12:00", occupancy: 95 },
    { hour: "13:00", occupancy: 88 },
    { hour: "14:00", occupancy: 82 },
    { hour: "15:00", occupancy: 75 },
    { hour: "16:00", occupancy: 66 },
];
const chartConfig = {
    occupancy: {
        label: "Occupancy Percentage",
        color: "hsl(var(--chart-2))",
    },
    mobile: {
        label: "Mobile",
        color: "hsl(var(--chart-2))",
    },
} satisfies ChartConfig

export function OccupancyPlot() {

    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())
    return (
        <Card>
            <CardHeader>
                <CardTitle>Occupancy Percentage</CardTitle>
                <CardDescription>Indicates how much of the parking capacity is in use over time. Helps monitor usage and optimize space management.</CardDescription>
                <DatePickerFromTo from={fromDate} setFrom={setFromDate} to={toDate} setTo={setToDate} />

            </CardHeader>
            <CardContent>
                <ChartContainer config={chartConfig}>
                    <LineChart
                        accessibilityLayer
                        data={chartData}
                        margin={{
                            left: 12,
                            right: 12,
                        }}
                    >
                        <CartesianGrid vertical={false} />
                        <XAxis
                            dataKey="hour"
                            tickLine={false}
                            axisLine={false}
                            tickMargin={8}
                            tickFormatter={(value) => value.slice(0, 3)}
                        />
                        <ChartTooltip
                            cursor={false}
                            content={<ChartTooltipContent hideLabel />}
                        />
                        <Line
                            dataKey="occupancy"
                            type="natural"
                            stroke="var(--color-occupancy)"
                            strokeWidth={2}
                            dot={{
                                fill: "var(--color-occupancy)",
                            }}
                            activeDot={{
                                r: 6,
                            }}
                        />
                    </LineChart>
                </ChartContainer>
            </CardContent>
            <CardFooter className="flex-col items-start gap-2 text-sm">
                {/*<span>Some information in footer</span>*/}
            </CardFooter>
        </Card>
    )
}
