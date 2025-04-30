import ReviewCard from "@/components/cards/reviewCard.tsx";
import {IReview} from "@/models/common/IReview.ts";
import {useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {IPage} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import SmallLoader from "@/components/ui/small-loader.tsx";
import {Button} from "@/components/ui/button.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";


function Reviews() {



    const {parkingId} = useParams<{ parkingId: string }>();


    const {reviewStore} = useContext(Context);

    const [loading, setLoading] = useState<boolean>(false);



    const [reviews, setReviews] = useState<IPage<IReview>>({} as IPage<IReview>);

    const [page, setPage] = useState(0);
    const [pageSize] = useState(5);



    useEffect(() => {
        console.log("Change  page or page size",page, pageSize);
    }, [page, pageSize]);




    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching reviews for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await reviewStore.fetchReviews(parkingId, page, pageSize);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }



                if (reviewStore.reviewsPage) {
                    setReviews(reviewStore.reviewsPage as IPage<IReview>);
                }

            } catch (error) {
                console.error("Failed to fetch reviews:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, page, pageSize, reviewStore]);





    return <div className="flex flex-col justify-start w-full h-full m-8">

        <PageHeader title="Reviews: Feedback That Drives Success 🚗⭐" subtitle="Welcome to the Access Reviews section of your Parking Dashboard! Here, you can check all the feedback and insights related to parking access."/>

        <div className="cardContainer flex flex-col gap-4 mt-6">
            {loading ? <SmallLoader className={"min-h-[400px] py-10"} />
                : reviews?.content?.length > 0 ?
                                                reviews.content.map((review : IReview) => {
                                                    return <ReviewCard data={review} key={review.id}/>
                                                })
                    : <div>No reviews</div>
            }
        </div>
        <div className="p-4">
            <Button
                variant="ghost"
                disabled={reviews.first}
                onClick={() => setPage(reviews?.number - 1)}
            >
                Previous
            </Button>
            <Button
                variant="ghost"
                disabled={reviews.last}
                onClick={() => setPage(reviews?.number + 1)}
            >
                Next
            </Button>
            <span className="text-sm">
                        {/*Page {table.getState().pagination.pageIndex + 1} of {table.getPageCount() !== 0 ? table.getPageCount() : 1}*/}
                Page {reviews?.number + 1} of {reviews?.totalPages !== 0 ? reviews?.totalPages : 1}
                    </span>
        </div>
    </div>;
}

export default Reviews;