import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"

//@ts-ignore
function SelectSortTypeShadcn({options, defaultOption, value, onChange}) {
    return <Select defaultValue={defaultOption} value={value} onValueChange={onChange}>
        <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="Sort Type" />
        </SelectTrigger>
        <SelectContent>
            {/*//@ts-ignore*/}
            {options.map(option => (
                <SelectItem key={option.key} value={option.key}>{option.value}</SelectItem>
            ))}
        </SelectContent>
    </Select>

}
export default SelectSortTypeShadcn;