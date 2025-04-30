import {DatePicker} from "@/components/ui/date-picker.tsx";


export default function DatePickerFromTo({from, setFrom, to, setTo} : {from: Date,setFrom : (arg0: Date) => void , to: Date, setTo : (arg0: Date) => void}) {

    return (
        <div
            className="flex flex-row flex-nowrap items-center justify-evenly w-full  gap-4 rounded-lg bg-white p-6">
            <span> From </span> <DatePicker date={from} setDate={setFrom}/>
            <span> To </span><DatePicker date={to} setDate={setTo}/>
        </div>
    )
}