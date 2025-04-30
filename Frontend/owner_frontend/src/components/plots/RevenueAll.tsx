
import { GitCommitVertical } from "lucide-react"
import { CartesianGrid, Line, LineChart, XAxis } from "recharts"
import {
    Card,
    CardContent,
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
    { month: "January", desktop: 186, mobile: 80 },
    { month: "February", desktop: 305, mobile: 200 },
    { month: "March", desktop: 237, mobile: 120 },
    { month: "April", desktop: 73, mobile: 190 },
    { month: "May", desktop: 209, mobile: 130 },
    { month: "June", desktop: 214, mobile: 140 },
]
const chartConfig = {
    desktop: {
        label: "Amount",
        color: "hsl(var(--chart-1))",
    },
    mobile: {
        label: "Parking 2",
        color: "hsl(var(--chart-2))",
    },
} satisfies ChartConfig

function RevenueAllPlot(){


    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())


    return ( <Card>
        <CardHeader>
            <CardTitle>Revenue</CardTitle>
            {/*<CardDescription>January - February 2025</CardDescription>*/}
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
            <div className="leading-none text-muted-foreground  text-left mt-4">
                Shows total earnings from all parking locations over time. Helps track overall performance, spot trends, and support business decisions.
            </div>
        </CardFooter>
    </Card>)
}
export default RevenueAllPlot;