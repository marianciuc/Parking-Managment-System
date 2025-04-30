
import * as React from "react"

import { Label, Pie, PieChart } from "recharts"

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
const chartData = [
    { browser: "chrome", visitors: 546.09, fill: "var(--color-chrome)" },
    { browser: "safari", visitors: 249.87, fill: "var(--color-safari)" },
    // { browser: "firefox", visitors: 287, fill: "var(--color-firefox)" },
    // { browser: "edge", visitors: 173, fill: "var(--color-edge)" },
    // // { browser: "other", visitors: 190, fill: "var(--color-other)" },
]

const chartConfig = {
    visitors: {
        label: "Money",
    },
    chrome: {
        label: "Parking 1: ",
        color: "hsl(var(--chart-1))",
    },
    safari: {
        label: "Parking 2: ",
        color: "hsl(var(--chart-2))",
    },
} satisfies ChartConfig


function RevenueSharePlot(){

    const totalVisitors = React.useMemo(() => {
        return chartData.reduce((acc, curr) => acc + curr.visitors, 0)
    }, [])
    const [fromDate, setFromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    const [toDate, setToDate] = React.useState<Date>(new Date())

    return (  <Card className="flex flex-col">
        <CardHeader className="items-center pb-0">
            <CardTitle>Revenue Distribution by Parking</CardTitle>
            {/*<CardDescription>January - June 2024</CardDescription>*/}
            <DatePickerFromTo from={fromDate} setFrom={setFromDate} to={toDate} setTo={setToDate} />
        </CardHeader>
        <CardContent className="flex-1 pb-0">
            <ChartContainer
                config={chartConfig}
                className="mx-auto aspect-square max-h-[250px]"
            >
                <PieChart>
                    <ChartTooltip
                        cursor={false}
                        content={<ChartTooltipContent hideLabel />}
                    />
                    <Pie
                        data={chartData}
                        dataKey="visitors"
                        nameKey="browser"
                        innerRadius={60}
                        strokeWidth={5}
                    >
                        <Label
                            content={({ viewBox }) => {
                                if (viewBox && "cx" in viewBox && "cy" in viewBox) {
                                    return (
                                        <text
                                            x={viewBox.cx}
                                            y={viewBox.cy}
                                            textAnchor="middle"
                                            dominantBaseline="middle"
                                        >
                                            <tspan
                                                x={viewBox.cx}
                                                y={viewBox.cy}
                                                className="fill-foreground text-3xl font-bold"
                                            >
                                                {totalVisitors.toLocaleString()}
                                            </tspan>
                                            <tspan
                                                x={viewBox.cx}
                                                y={(viewBox.cy || 0) + 24}
                                                className="fill-muted-foreground"
                                            >
                                                Total
                                            </tspan>
                                        </text>
                                    )
                                }
                            }}
                        />
                    </Pie>
                </PieChart>
            </ChartContainer>
        </CardContent>
        <CardFooter className="flex-col gap-2 text-sm">
            {/*<div className="flex items-center gap-2 font-medium leading-none">*/}
            {/*    Trending up by 5.2% this month <TrendingUp className="h-4 w-4" />*/}
            {/*</div>*/}
            <div className="leading-none text-muted-foreground text-left">
                Displays the percentage of total revenue contributed by each parking location. Helps identify top-performing and underperforming sites.
            </div>
        </CardFooter>
    </Card>)
}
export default RevenueSharePlot;