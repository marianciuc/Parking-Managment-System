import {ChevronsUpDown, Plus} from "lucide-react"

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {IParking} from "@/models/common";
import {Button} from "@/components/ui/button.tsx";

export function ParkingSwitcher({
                                    parkingList,
                                    activeParking,
                                    setActiveParking,
                                    createParking
                                }: {
    parkingList: IParking[],
    activeParking: IParking | null,
    setActiveParking: (parking: IParking) => void,
    createParking: () => void,
}) {

    return (
        <div className="w-full">
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button
                        size="lg"
                        className="sidebar-header w-full rounded-none"
                    >
                        <div
                            className="flex aspect-square size-8 items-center justify-center rounded-lg">
                            <img
                                        src="/logo_white.svg"
                                        alt="logo"
                                        className="h-8 w-8 mr-3"
                                    />
                        </div>
                        <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-semibold">
                  {activeParking?.name ?? "Select a parking"}
                </span>
                            <span className="truncate text-xs">{activeParking?.name}</span>
                        </div>
                        <ChevronsUpDown className="ml-auto"/>
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent
                    className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
                    align="start"
                    sideOffset={4}
                >
                    <DropdownMenuLabel className="text-xs text-muted-foreground">
                        Your parking spaces
                    </DropdownMenuLabel>
                    {parkingList.map((parking) => (
                        <DropdownMenuItem
                            key={parking.id}
                            onClick={() => setActiveParking(parking)}
                            className="gap-2 p-2"
                        >
                            {parking.name}
                        </DropdownMenuItem>
                    ))}
                    <DropdownMenuSeparator/>
                    <DropdownMenuItem className="gap-2 p-2">
                        <div className="flex size-6 items-center justify-center rounded-md border bg-background">
                            <Plus className="size-4"/>
                        </div>
                        <div className="font-medium text-muted-foreground" onClick={createParking}>Create parking</div>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    )
}
