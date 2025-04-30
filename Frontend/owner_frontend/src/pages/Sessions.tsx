import "./styles/sessions.css";
import SessionDataTable from "@/components/blocks/tables/SessionDataTable.tsx";
import SessionMetrics from "@/components/blocks/SessionMetrics.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";
import { motion } from "framer-motion";

function Sessions() {


    return (
        <div className="flex flex-col items-center justify-start gap-10 h-full px-6 py-6 m-8  w-full rounded-lg font-sans min-h-screen">
            {/* Заголовок */}
            <PageHeader title="Parking Sessions" subtitle="Monitor and manage all active and past parking sessions in your parking
                    lot. Use this page to gain insights into how your facility is being
                    utilized, track vehicle movements, and resolve any outstanding issues.
                    Stay in control by viewing real-time session data or filtering past
                    sessions for analysis.">
                <SessionMetrics/>
            </PageHeader>


            <motion.div className="bg-white shadow-md rounded-xl  flex flex-col justify-between border w-full"
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}>
                <SessionDataTable/>
            </motion.div>


        </div>
    );
}

export default Sessions;