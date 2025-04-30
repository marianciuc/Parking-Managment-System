
const DataNotAvailable = () => {
    return (
        <div className="flex flex-col items-center justify-center min-h-[200px] bg-transparent  p-6">
            <div className="flex space-x-2">
                <div className="w-8 h-1.5 bg-gray-400 rounded-full" />
                <div className="w-8 h-1.5 bg-gray-400 rounded-full" />
                <div className="w-8 h-1.5 bg-gray-400 rounded-full" />
            </div>
            <p className="mt-4 text-gray-500 text-sm tracking-widest">
                Data Not Available
            </p>
        </div>
    );
};

export default DataNotAvailable;
