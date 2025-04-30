import {X} from "lucide-react";
import {Badge} from "@/components/ui/badge.tsx";

function Tag({value, onClose, className=""}:{value: string; onClose?: () => void; className?: string}) {
    return (
        <Badge  className={`p-1 pl-4 flex flex-row flex-nowrap w-fit gap-3 rounded-2xl ${className}`}>
            {value}
        <X onClick={onClose}/>
    </Badge>
    )
}

export default Tag;