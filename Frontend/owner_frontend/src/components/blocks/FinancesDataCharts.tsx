import * as React from "react"

import RevenueSharePlot from "@/components/plots/revenue_share_plot.tsx";

import RevenueAllPlot from "@/components/plots/RevenueAll.tsx";




function FinancesDataCharts() {
    const [fromDate] = React.useState<Date>(new Date());
    fromDate.setDate(fromDate.getDate() - 7);
    // const [toDate, setToDate] = React.useState<Date>(new Date())
    //
    // const {userStore, paymentsStore} = useContext(Context);
    //
    // const [loading, setLoading] = useState<boolean>(false);

    // useEffect(() => {
    //     const fetchData = async () => {
    //         setLoading(true);
    //         try {
    //             if (!userStore.user.userId) {
    //                 console.warn("No user passed");
    //                 return;
    //             }
    //
    //             console.log("Fetching user balance for userId:", userStore.user.userId);
    //
    //             const toastMessageIncomesByDays: IToastInfo | void = await paymentsStore.getIncomesByDays(userStore.user.userId, fromDate, toDate);
    //             const toastMessageIncomesByDayTime: IToastInfo | void = await paymentsStore.getIncomesByDayStats(userStore.user.userId, fromDate, toDate);
    //             const toastMessageRevenue: IToastInfo | void = await paymentsStore.getRevenue(userStore.user.userId, fromDate, toDate);
    //             const toastMessageIncomesByDayStats: IToastInfo | void = await paymentsStore.getIncomesByDayStats(userStore.user.userId, fromDate, toDate);
    //
    //
    //             //
    //             // if (toastMessage) {
    //             //     console.log("Toast Message:", toastMessage);
    //             // }
    //
    //
    //             // if (paymentsStore.accountBalanceStats) {
    //             //     setBalanceInfo(paymentsStore.accountBalanceStats);
    //             // }
    //
    //         } catch (error) {
    //             console.error("Failed to fetch reviews:", error);
    //         }
    //         setLoading(false);
    //     };
    //
    //     fetchData();
    // }, [fromDate, paymentsStore, toDate, userStore.user.userId]);


    return (
        <>
            {/*<DatePickerFromTo from={fromDate} setFrom={setFromDate} to={toDate} setTo={setToDate} />*/}
            <div className="grid grid-cols-2 md:grinpmd-cols-2 gap-4 mt-6">

                <RevenueSharePlot/>
                <RevenueAllPlot/>
                {/*<RevenuePlot/>*/}
                {/*<RevenuePlot/>*/}

                {/*<Card>*/}
                {/*    <CardContent>*/}
                {/*        <h2 className="text-lg font-semibold">Доходы по дням</h2>*/}
                {/*        <Line data={lineData} />*/}
                {/*    </CardContent>*/}
                {/*</Card>*/}

                {/*<Card>*/}
                {/*    <CardContent>*/}
                {/*        <h2 className="text-lg font-semibold">Доля дохода</h2>*/}
                {/*        <Pie data={pieData} />*/}
                {/*    </CardContent>*/}
                {/*</Card>*/}

                {/*<Card>*/}
                {/*    <CardContent>*/}
                {/*        <h2 className="text-lg font-semibold">Доходы по дням</h2>*/}
                {/*        <Bar data={barData} />*/}
                {/*    </CardContent>*/}
                {/*</Card>*/}
            </div>
        </>
    )
}

export {FinancesDataCharts};