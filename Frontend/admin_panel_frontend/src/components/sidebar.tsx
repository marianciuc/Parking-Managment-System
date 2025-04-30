import Link from "next/link";
import {
    DollarSign as BalanceIcon,
    FileText as ReportsIcon,
    Home as HomeIcon,
    Key as KeyIcon,
    MessageSquare as ReviewsIcon,
    Package as PricingIcon,
    Users as UsersIcon
} from "lucide-react";
import React from "react";

const navItems = [
    { href: "/dashboard", label: "Main", Icon: HomeIcon },
    { href: "/dashboard/balance", label: "System Balance", Icon: BalanceIcon },
    { href: "/dashboard/accounts", label: "Accounts", Icon: UsersIcon },
    { href: "/dashboard/parking", label: "Parking Management", Icon: PricingIcon },
    { href: "/dashboard/reviews", label: "Reviews Moderation", Icon: ReviewsIcon },
    { href: "/dashboard/tags", label: "Tags Management", Icon: ReportsIcon },
    { href: "/dashboard/api-keys", label: "API-Keys", Icon: KeyIcon },
];

const NavItem = ({ href, label, Icon }: { href: string; label: string; Icon: React.ComponentType<{ className?: string }> }) => (
    <li>
        <Link href={href} className="flex flex-row h-10 overflow-hidden flex-nowrap items-center gap-3 px-4 py-2 rounded hover:bg-gray-700">
            <Icon className="w-5 h-5" />
            {label}
        </Link>
    </li>
);

export default function Sidebar() {
    return (
        <aside className="bg-gray-800 text-white min-h-screen ">
            <div className="p-4 text-center text-xl font-bold border-b border-gray-700">
                HandyParking Admin Panel
            </div>
            <nav className="p-4">
                <ul className="space-y-2">
                    {navItems.map(({ href, label, Icon }) => (
                        <NavItem key={href} href={href} label={label} Icon={Icon} />
                    ))}
                </ul>
            </nav>
        </aside>
    );
}