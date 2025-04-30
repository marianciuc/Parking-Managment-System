
import { GitCommitVertical } from "lucide-react"
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
    { date: "2025-04-07", desktop: 180, mobile: 120 },
    { date: "2025-04-06", desktop: 190, mobile: 110 },
    { date: "2025-04-05", desktop: 202, mobile: 130 },
    { date: "2025-04-04", desktop: 143, mobile: 105 },
    { date: "2025-04-03", desktop: 160, mobile: 95 },
    { date: "2025-04-02", desktop: 150, mobile: 90 },
    { date: "2025-04-01", desktop: 140, mobile: 85 },
];
const chartConfig = {
    desktop: {
        label: "Revenue",
        color: "hsl(var(--chart-1))",
    },
    mobile: {
        label: "Mobile",
        color: "hsl(var(--chart-2))",
    },
} satisfies ChartConfig

function RevenuePlot(){


    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())


    return ( <Card>
        <CardHeader>
            <CardTitle>Revenue</CardTitle>
            <CardDescription>Shows earnings over time for a specific parking location. Useful for tracking performance and spotting changes.</CardDescription>
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
                        dataKey="month"
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
                        dataKey="desktop"
                        type="natural"
                        stroke="var(--color-desktop)"
                        strokeWidth={2}
                        dot={({ cx, cy, payload }) => {
                            const r = 24
                            return (
                                <GitCommitVertical
                                    key={payload.month}
                                    x={cx - r / 2}
                                    y={cy - r / 2}
                                    width={r}
                                    height={r}
                                    fill="hsl(var(--background))"
                                    stroke="var(--color-desktop)"
                                />
                            )
                        }}
                    />
                </LineChart>
            </ChartContainer>
        </CardContent>
        <CardFooter className="flex-col items-start gap-2 text-sm">
            {/*<div className="flex gap-2 font-medium leading-none">*/}
            {/*    Trending up by 5.2% this month <TrendingUp className="h-4 w-4" />*/}
            {/*</div>*/}
            {/*<div className="leading-none text-muted-foreground">*/}
            {/*    Showing total visitors for the last 6 months*/}
            {/*</div>*/}
        </CardFooter>
    </Card>)
}
export default RevenuePlot;