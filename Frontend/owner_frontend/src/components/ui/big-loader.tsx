import {motion} from "framer-motion";

function BigLoader(){
    return(
        <div
            className="flex items-center justify-center w-full h-screen bg-gradient-to-br from-gray-900 to-gray-800">
            <div className="relative flex flex-col items-center">
                <motion.div
                    className="relative flex space-x-2"
                    initial={{opacity: 0}}
                    animate={{opacity: 1}}
                    transition={{duration: 1}}
                >
                    <motion.div
                        className="w-12 h-2 bg-white rounded-full"
                        animate={{x: [0, 20, 0]}}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                        }}
                    />
                    <motion.div
                        className="w-12 h-2 bg-white rounded-full"
                        animate={{x: [0, 20, 0]}}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                            delay: 0.2,
                        }}
                    />
                    <motion.div
                        className="w-12 h-2 bg-white rounded-full"
                        animate={{x: [0, 20, 0]}}
                        transition={{
                            repeat: Infinity,
                            duration: 1.2,
                            ease: "easeInOut",
                            delay: 0.4,
                        }}
                    />
                </motion.div>
                <p className="mt-4 text-gray-300 text-sm tracking-widest">
                    Loading parking
                </p>
            </div>
        </div>
    )
}
export default BigLoader;