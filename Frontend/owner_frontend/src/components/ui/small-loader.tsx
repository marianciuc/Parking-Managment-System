import { motion } from "framer-motion";
import React from "react";
import {cn} from "@/lib/utils.ts"

type LoaderProps = {
    className?: string;
};

const SmallLoader : React.FC<LoaderProps> = ({className}) => {
    return (
        <div className= {cn("flex items-center justify-center w-full h-20 bg-white rounded-lg ",className)}>
            <div className="relative flex flex-col items-center">
                <motion.div
                    className="relative flex space-x-2"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 1 }}
                >
                    <motion.div
                        className="w-8 h-1.5 bg-gray-500 rounded-full"
                        animate={{ x: [0, 15, 0] }}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                        }}
                    />
                    <motion.div
                        className="w-8 h-1.5 bg-gray-500 rounded-full"
                        animate={{ x: [0, 15, 0] }}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                            delay: 0.2,
                        }}
                    />
                    <motion.div
                        className="w-8 h-1.5 bg-gray-500 rounded-full"
                        animate={{ x: [0, 15, 0] }}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                            delay: 0.4,
                        }}
                    />
                </motion.div>
                <p className="mt-2 text-gray-500 text-xs tracking-widest">
                    Loading data...
                </p>
            </div>
        </div>
    );
};

export default SmallLoader;
