import {makeAutoObservable} from "mobx";
import {IPage, IReview} from "@/models/common";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import ReviewService from "@/services/ReviewService.ts";

export default class ReviewStore{
    reviewsPage : IPage<IReview> ={} as IPage<IReview>;
    constructor(){
        makeAutoObservable(this);
    }


    async fetchReviews(parkingId : string,
                          page?:number,
                          size?:number,): Promise<IToastInfo | void> {
        console.log("Fetching reviews data");
        try {
            const response = await ReviewService.fetchRecords(parkingId, page,size);
            console.log(response);
            this.reviewsPage = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async reportReview(reviewId:string) {
        console.log("Report review data");
        try{
            const response = await ReviewService.reportReview(reviewId);
            console.log(response);
            if(response.status === 200){
                return  {
                    message: "Review successfully reported",
                    type: ToastType.SUCCESS,
                };
            }
        }catch(e :unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            }
        }
    }


}
