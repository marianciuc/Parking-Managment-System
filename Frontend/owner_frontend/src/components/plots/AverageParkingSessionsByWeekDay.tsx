import { TrendingUp } from "lucide-react"
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts"

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
import * as React from "react";
import DatePickerFromTo from "@/components/DatePickerFromTo.tsx";
const chartData = [
    { browser: "monday", visitors: 55, fill: "var(--color-monday)" },
    { browser: "tuesday", visitors: 43, fill: "var(--color-tuesday)" },
    { browser: "wednesday", visitors: 47, fill: "var(--color-wednesday)" },
    { browser: "thursday", visitors: 56, fill: "var(--color-thursday)" },
    { browser: "friday", visitors: 44, fill: "var(--color-friday)" },
    { browser: "saturday", visitors: 37, fill: "var(--color-saturday)" },
    { browser: "sunday", visitors: 35, fill: "var(--color-sunday)" },
]

const chartConfig = {
    visitors: {
        label: "Vehicles",
    },
    monday: {
        label: "Monday",
        color: "hsl(var(--chart-1))",
    },
    tuesday: {
        label: "Tuesday",
        color: "hsl(var(--chart-2))",
    },
    wednesday: {
        label: "Wednesday",
        color: "hsl(var(--chart-3))",
    },
    thursday: {
        label: "Thursday",
        color: "hsl(var(--chart-4))",
    },
    friday: {
        label: "Friday",
        color: "hsl(var(--chart-5))",
    },
    saturday: {
        label: "Saturday",
        color: "hsl(var(--chart-2))",
    },
    sunday: {
        label: "Sunday",
        color: "hsl(var(--chart-1))",
    },
} satisfies ChartConfig

function AverageParkingSessionsByWeekDay(){

    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())
    return (
        <Card>
            <CardHeader>
                <CardTitle>Average Parking Sessions by day of week</CardTitle>
                <CardDescription>
                    Displays the average number of parking sessions for each weekday. Helps identify peak and low-traffic days.
                </CardDescription>
                <DatePickerFromTo from={fromDate} setFrom={setFromDate} to={toDate} setTo={setToDate} />

            </CardHeader>
            <CardContent>
                <ChartContainer config={chartConfig}>
                    <BarChart accessibilityLayer data={chartData}>
                        <CartesianGrid vertical={false} />
                        <XAxis
                            dataKey="browser"
                            tickLine={false}
                            tickMargin={10}
                            axisLine={false}
                            tickFormatter={(value) =>
                                chartConfig[value as keyof typeof chartConfig]?.label
                            }
                        />
                        <ChartTooltip
                            cursor={false}
                            content={<ChartTooltipContent hideLabel />}
                        />
                        <Bar
                            dataKey="visitors"
                            strokeWidth={2}
                            radius={8}
                            activeIndex={2}
                           
                        />
                    </BarChart>
                </ChartContainer>
            </CardContent>
            <CardFooter className="flex-col items-start gap-2 text-sm">
                <div className="flex gap-2 font-medium leading-none">
                    Trending up by 5.2% this month <TrendingUp className="h-4 w-4" />
                </div>
                <div className="leading-none text-muted-foreground">
                    Showing total visitors for the last 6 months
                </div>
            </CardFooter>
        </Card>)
}
export default AverageParkingSessionsByWeekDay