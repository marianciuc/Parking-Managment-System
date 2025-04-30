import $api from "@/http"
import {AxiosResponse} from "axios";
import {IPage, IReview} from "@/models/common";

const use_mock = import.meta.env.VITE_USE_MOCK;

export default class ReviewService {
    static async fetchRecords(parkingId:string ,
                                                 page:number = 0,
                                                 size:number = 5,


    ): Promise<AxiosResponse<IPage<IReview>>> {
        if(use_mock) {

            const content: IReview[] = [
                { id: "1", icon_url: "https://example.com/icon1.png", name: "John Doe", message: "This parking lot is hands down one of the most convenient ones I’ve used in the area. There are always plenty of available spots, even during busier hours, and the layout makes it easy to navigate. The spaces are clearly marked and wide enough that you don’t feel like you’re squeezing in, which is great if you have a bigger car or SUV. I also appreciate how close it is to the main entrance—just a quick walk and you’re there. The lighting is solid, and I’ve never felt unsafe even when leaving after dark. Definitely a reliable place to park when I’m in the neighborhood.", date: [2025, 3, 1, 14, 30, 0, 500] },
                { id: "2", icon_url: "https://example.com/icon2.png", name: "Jane Smith", message: "This lot is decent, but it has its drawbacks. It serves its purpose if you’re just looking for somewhere to leave your car for a short time, but I wouldn’t rely on it during peak hours. The spaces are a little tight, and maneuvering through the rows can feel cramped, especially if a few cars aren’t parked straight. There’s also limited signage, so it’s not the easiest place to figure out if you’re coming for the first time. That said, I haven’t had any major issues, and it’s generally clean and well-maintained. Just manage your expectations, especially if you’re in a rush or driving a larger vehicle.", date: [2025, 3, 2, 9, 15, 30, 200] },
                { id: "3", icon_url: "https://example.com/icon3.png", name: "Alice Johnson", message: "I’m genuinely impressed with how smooth and stress-free this parking experience was, all thanks to their fantastic online system. From the moment I arrived, everything felt incredibly modern and user-friendly. I was able to reserve my spot in advance through the website, which was intuitive and easy to navigate. The online payment system is quick, secure, and supports multiple payment methods—which is a huge plus in my book.\n" +
                        "\n" +
                        "What really stood out to me, though, was the real-time session tracking. I could see exactly how long I had been parked, how much I was being charged, and even get reminders when my session was about to end. No more guessing or racing back to feed a meter—everything is managed through my phone. I extended my time with just a couple of taps.\n" +
                        "\n" +
                        "It’s clear that the system was designed with the user in mind. There’s no need to download an app, no paper tickets, and no confusion about pricing. It’s efficient, transparent, and a total game changer for anyone tired of outdated parking setups. I wish more places used a system like this—it’s the future of parking.", date: [2025, 3, 3, 16, 45, 10, 800] },
                { id: "4", icon_url: "https://example.com/icon4.png", name: "Bob Brown", message: "This was one of the best parking experiences I've had in a long time. The lot itself is clean, spacious, and well-organized, with wide, clearly marked spaces and smooth traffic flow. It’s located in a super convenient area—just a short walk from shops and restaurants—which made it perfect for my visit. Security is clearly a priority too: there are surveillance cameras and good lighting throughout, which gave me peace of mind leaving my car there for a few hours.\n" +
                        "\n" +
                        "But the real highlight was the online parking system. I was able to book a spot ahead of time through their website, and the entire process took under a minute. Payment was handled online, so there was no need to fumble with cash or worry about finding a pay station. The system also tracks your session in real-time, which is incredibly helpful. I got a notification when my session was about to expire and extended it instantly with just a tap on my phone—no need to rush back or worry about getting a ticket.\n" +
                        "\n" +
                        "Between the smooth layout of the lot and the modern tech behind the scenes, this is exactly how parking should work in 2025. Efficient, user-friendly, and stress-free. Highly recommend it!", date: [2025, 3, 4, 10, 0, 5, 300] },
                { id: "5", icon_url: "https://example.com/icon5.png", name: "Charlie White", message: "I loved it! Worth every cent.", date: [2025, 3, 5, 18, 25, 45, 900] }
            ];
            return Promise.resolve({
                data:{
                    content: content,
                    pagable: {
                        pageNumber: page,
                        pageSize: size,
                        sort: { empty: false, sorted: true, unsorted: false },
                        offset: page * size,
                        unpaged: false,
                        paged: true
                    },
                    totalPages: 5,
                    totalElements: 10,
                    last: page === 4,
                    size,
                    number: page,
                    sort: { empty: false, sorted: true, unsorted: false },
                    numberOfElements: content.length,
                    first: page === 0,
                    empty: content.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<IReview>>);
        }

        return $api.get<IPage<IReview>>(`/api/v1/reviews/search`,  {
            params: {
                page: page,
                size: size,
                parkingId: parkingId,
            },
        });
    }


    static async reportReview(reviewId:string){
        return $api.post(`/api/v1/review/${reviewId}/report`);
    }
}