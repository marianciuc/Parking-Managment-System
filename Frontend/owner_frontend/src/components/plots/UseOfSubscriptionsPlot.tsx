import { TrendingUp } from "lucide-react"
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


type DurationData = {
    label: string;
    averageDuration: number;
};

const generateDurationData = (): DurationData[] => {
    const data: DurationData[] = [];
    const now = new Date();

    for (let i = 24; i >= 1; i--) {
        const date = new Date(now.getTime() - i  * 60 * 60 * 1000); // Subtract 2 hours each step
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        const label = `${hours}:${minutes}`;
        const averageDuration = Math.floor(Math.random() * 100) + 15;
        data.push({ label, averageDuration });
    }

    return data;
};

const data = generateDurationData();

console.log(data)

const chartConfig = {
    averageDuration: {
        label: "Duration [m]:",
        color: "hsl(var(--chart-1))",
    },
    mobile: {
        label: "Mobile",
        color: "hsl(var(--chart-2))",
    },
} satisfies ChartConfig

function UseOfSubscriptionsPlot(){

    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())
    console.log(data)
    return ( <Card>
        <CardHeader>
            <CardTitle>Average Parking Session Duration</CardTitle>
            <CardDescription>Shows the average length of closed parking sessions in the past hour. Useful for understanding short-term usage patterns.</CardDescription>
            <DatePickerFromTo from={fromDate} setFrom={setFromDate} to={toDate} setTo={setToDate} />

        </CardHeader>
        <CardContent>
            <ChartContainer config={chartConfig}>
                <LineChart
                    accessibilityLayer
                    data={data}
                    margin={{
                        left: 12,
                        right: 12,
                    }}
                >
                    <CartesianGrid vertical={false} />
                    <XAxis
                        dataKey="label"
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
                        dataKey="averageDuration"
                        type="natural"
                        stroke="var(--color-averageDuration)"
                        strokeWidth={2}
                        dot={{
                            fill: "var(--color-averageDuration)",
                        }}
                        activeDot={{
                            r: 6,
                        }}
                    />
                </LineChart>
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
    </Card>);
}
export default UseOfSubscriptionsPlot;