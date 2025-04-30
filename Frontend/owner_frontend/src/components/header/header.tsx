import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {useContext, useEffect, useState} from "react";
import {Bell, ChevronDown} from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {Context} from "@/main.tsx";
import {IUser} from "@/models/common";
import {useNavigate} from "react-router-dom";



function Header () {

    const [user, setUser] = useState<IUser | null>(null)

    const navigate = useNavigate();

    const {userStore} = useContext(Context);

    useEffect(() => {
        if (userStore.user) setUser(userStore.user);
    }, [userStore]);

    return (
        <div className="h-[90px] header w-full flex flex-row justify-end items-center  gap-16 px-16 ">
            {/*<div className="">*/}
            {/*</div>*/}
            {/*<div className="flex items-center justify-center gap-3 w-1/3">*/}
            {/*</div>*/}
            {/*<SidebarTrigger />*/}
            <div className="cursor-pointer hover:text-gray-200" onClick={() => navigate('/finances')}>
                Finances
            </div>
            <Bell  color="white"/>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <button className="flex flex-row items-center justify-center gap-3 hover:text-gray-200">
                <span>
                    {user?.firstName} {user?.lastName}
                </span>
                        <Avatar>
                            <AvatarImage src="/logo_blue.svg" className="w-2/4 ml-1"/>
                            <AvatarFallback>CN</AvatarFallback>
                        </Avatar>
                        <ChevronDown color="white" size={20}/>
                    </button>
                </DropdownMenuTrigger>
                <DropdownMenuContent  className="w-56">

                    <DropdownMenuLabel>My Account</DropdownMenuLabel>
                    <DropdownMenuSeparator/>
                    <DropdownMenuGroup>
                        <DropdownMenuItem onClick={()=>navigate("/profile")}>
                            Profile
                        </DropdownMenuItem>
                    </DropdownMenuGroup>
                    <DropdownMenuSeparator />
                    {/*<DropdownMenuGroup>*/}
                    {/*    <DropdownMenuItem>Team</DropdownMenuItem>*/}
                    {/*    <DropdownMenuSub>*/}
                    {/*        <DropdownMenuSubTrigger>Invite users</DropdownMenuSubTrigger>*/}
                    {/*        <DropdownMenuPortal>*/}
                    {/*            <DropdownMenuSubContent>*/}
                    {/*                <DropdownMenuItem>Email</DropdownMenuItem>*/}
                    {/*                <DropdownMenuItem>Message</DropdownMenuItem>*/}
                    {/*                <DropdownMenuSeparator />*/}
                    {/*                <DropdownMenuItem>More...</DropdownMenuItem>*/}
                    {/*            </DropdownMenuSubContent>*/}
                    {/*        </DropdownMenuPortal>*/}
                    {/*    </DropdownMenuSub>*/}
                    {/*    <DropdownMenuItem>*/}
                    {/*        New Team*/}
                    {/*        <DropdownMenuShortcut>⌘+T</DropdownMenuShortcut>*/}
                    {/*    </DropdownMenuItem>*/}
                    {/*</DropdownMenuGroup>*/}
                    {/*<DropdownMenuSeparator />*/}
                    {/*<DropdownMenuItem>GitHub</DropdownMenuItem>*/}
                    <DropdownMenuItem>Support</DropdownMenuItem>
                    {/*<DropdownMenuItem disabled>API</DropdownMenuItem>*/}
                    {/*<DropdownMenuSeparator />*/}
                    <DropdownMenuItem onClick={() => userStore.logout()}>
                        Log out
                        {/*<DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>*/}
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    )
}

export default Header;