import { motion } from "framer-motion";
import React from "react";


interface PageHeaderProps {
    title: string;
    subtitle: string;
    backgroundImageUrl?: string;
    children?: React.ReactNode;
}

function PageHeader({ title, subtitle,backgroundImageUrl, children }: PageHeaderProps) {
    return (<><div className="relative z-10 min-h-[420px] flex flex-col items-center justify-center gap-16 mb-6 w-full">
        <motion.div className="relative flex flex-col items-center justify-center gap-6 mt-20"
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3 }}>
            <h1 className="page-title">{title}</h1>
            <h2 className="page-subtitle">{subtitle}</h2>
        </motion.div>
        {children}
        </div>

        <div className="page_header_background">
            <img src={backgroundImageUrl ? backgroundImageUrl : `/public/default_background.webp`} alt="" className="w-full h-full" />
            <div className="absolute top-0 left-0 w-full h-full bg-black opacity-50"></div>
        </div>
    </>)
}
export default PageHeader;