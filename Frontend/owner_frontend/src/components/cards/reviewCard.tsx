import {IReview} from "@/models/common/IReview.ts";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {EllipsisIcon} from "lucide-react";
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import PrettyDate from "@/components/ui/pretty-date.tsx";
import {useContext} from "react";
import {Context} from "@/main.tsx";
import {ToastType} from "@/models/common/IToastInfo.ts";
import {toast} from "@/hooks/use-toast.ts";
import { motion } from "framer-motion";



function getFirstCharacter(str: string): string {
    return str.charAt(0) + str.charAt(str.indexOf(" ")+1);
}



function ReviewCard({data}: {data: IReview}) {

    const {reviewStore} = useContext(Context);

    const reportButton = async()=>{
        const response = await reviewStore.reportReview(data.id);
        if(response){
            toast({
                title: "Review report",
                description: response.message,
                variant: response.type == ToastType.ERROR ? "destructive" : "default"
            })
        }
    }

    return (
        <motion.article className="relative flex flex-row w-full justify-center px-4 items-start py-8 bg-white min-h-24 gap-3 rounded-2xl border border-gray-200"
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}>
            <div className="mx-4">
                <Avatar className="w-[75px] h-[75px]">
                    <AvatarImage src={data.icon_url}/>
                    <AvatarFallback>{getFirstCharacter(data.name)}</AvatarFallback>
                </Avatar>
            </div>
            <div className="grow flex items-start justify-start flex-col gap-1">
                <b>{data.name}</b>
                <p className={"text-start"}>
                    {data.message}
                </p>
            </div>
            <Popover>
                <PopoverTrigger className="absolute right-5 top-2">
                    <EllipsisIcon  onClick={()=>{ console.log(data.name)}}/>
                </PopoverTrigger>
                <PopoverContent className="cursor-pointer" onClick={reportButton}>Report</PopoverContent>
            </Popover>

            <i className="absolute right-5 bottom-2">
                <PrettyDate timestamp={data.date}/>
            </i>
        </motion.article>
    )
}

export default ReviewCard;