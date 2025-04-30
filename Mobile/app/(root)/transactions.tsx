import React, { useEffect, useState } from "react";
import {
    View,
    Text,
    FlatList,
    TouchableOpacity,
    SafeAreaView,
    ActivityIndicator,
    StatusBar,
    Dimensions,
    Animated,
    Image
} from "react-native";
import { useNavigation, Stack } from "expo-router";
import { Ionicons, MaterialCommunityIcons } from "@expo/vector-icons";
import { BlurView } from "expo-blur";
import { LinearGradient } from "expo-linear-gradient";

// Transaction type
type Transaction = {
    id: string;
    amount: number;
    date: string;
    status: 'completed' | 'pending' | 'failed';
    type: 'deposit' | 'withdrawal' | 'payment';
};

// Group transactions by date
type GroupedTransactions = {
    title: string;
    data: Transaction[];
};

export default function TransactionsHistoryScreen() {
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [groupedTransactions, setGroupedTransactions] = useState<GroupedTransactions[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [activeFilter, setActiveFilter] = useState<string>("all");
    const navigation = useNavigation();
    const scrollY = new Animated.Value(0);

    // Effect for creating dynamic header opacity
    const headerOpacity = scrollY.interpolate({
        inputRange: [0, 60],
        outputRange: [0, 1],
        extrapolate: 'clamp',
    });

    useEffect(() => {
        // Here should be an API call to get transaction history
        const fetchTransactions = async () => {
            try {
                // Sample test data (extended)
                const demoTransactions: Transaction[] = [
                    {
                        id: '1',
                        amount: 150,
                        date: '2023-10-15T12:00:00Z',
                        status: 'completed',
                        type: 'deposit'
                    },
                    {
                        id: '2',
                        amount: 10,
                        date: '2023-10-15T14:30:00Z',
                        status: 'completed',
                        type: 'payment'
                    },
                    {
                        id: '3',
                        amount: 150,
                        date: '2023-10-05T09:15:00Z',
                        status: 'pending',
                        type: 'deposit'
                    },
                    {
                        id: '5',
                        amount: 200,
                        date: '2023-09-25T10:20:00Z',
                        status: 'completed',
                        type: 'deposit'
                    },
                    {
                        id: '6',
                        amount: 23,
                        date: '2023-09-20T13:45:00Z',
                        status: 'completed',
                        type: 'payment'
                    },
                ];

                setTransactions(demoTransactions);
                groupTransactionsByDate(demoTransactions);
            } catch (error) {
                console.error("Failed to fetch transactions:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchTransactions();
    }, []);

    // Group transactions by date
    const groupTransactionsByDate = (transactions: Transaction[]) => {
        const groups: Record<string, Transaction[]> = {};

        transactions.forEach(transaction => {
            const date = new Date(transaction.date);
            const dateKey = date.toLocaleDateString('en-US', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            });

            if (!groups[dateKey]) {
                groups[dateKey] = [];
            }

            groups[dateKey].push(transaction);
        });

        const result: GroupedTransactions[] = Object.keys(groups).map(date => ({
            title: formatGroupDate(date),
            data: groups[date]
        }));

        // Sort groups by date (from newer to older)
        result.sort((a, b) => {
            const dateA = new Date(parseDate(a.title));
            const dateB = new Date(parseDate(b.title));
            return dateB.getTime() - dateA.getTime();
        });

        setGroupedTransactions(result);
    };

    // Parse date from format mm/dd/yyyy
    const parseDate = (dateStr: string) => {
        if (dateStr === "Today" || dateStr === "Yesterday") {
            const today = new Date();
            if (dateStr === "Yesterday") {
                today.setDate(today.getDate() - 1);
            }
            return today.toISOString().split('T')[0];
        }

        const [month, day, year] = dateStr.split('/');
        return `${year}-${month}-${day}`;
    };

    // Format group date
    const formatGroupDate = (dateStr: string) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const yesterday = new Date(today);
        yesterday.setDate(yesterday.getDate() - 1);

        const [month, day, year] = dateStr.split('/');
        const date = new Date(`${year}-${month}-${day}`);

        if (date.getTime() === today.getTime()) {
            return "Today";
        } else if (date.getTime() === yesterday.getTime()) {
            return "Yesterday";
        } else {
            return dateStr;
        }
    };

    // Format transaction time
    const formatTime = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getStatusLabel = (status: string) => {
        switch (status) {
            case 'completed':
                return 'Completed';
            case 'pending':
                return 'Processing';
            case 'failed':
                return 'Failed';
            default:
                return status;
        }
    };

    const getTypeLabel = (type: string) => {
        switch (type) {
            case 'deposit':
                return 'Account Deposit';
            case 'withdrawal':
                return 'Withdrawal';
            case 'payment':
                return 'Payment';
            default:
                return type;
        }
    };

    const getTypeIcon = (type: string) => {
        switch (type) {
            case 'deposit':
                return <MaterialCommunityIcons name="bank-transfer-in" size={22} color="#34C759" />;
            case 'withdrawal':
                return <MaterialCommunityIcons name="bank-transfer-out" size={22} color="#FF3B30" />;
            case 'payment':
                return <Ionicons name="card-outline" size={22} color="#007AFF" />;
            default:
                return <Ionicons name="wallet-outline" size={22} color="#8E8E93" />;
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'completed':
                return "#34C759";
            case 'pending':
                return "#FF9500";
            case 'failed':
                return "#FF3B30";
            default:
                return "#8E8E93";
        }
    };

    const filterTransactions = (filterType: string) => {
        setActiveFilter(filterType);

        if (filterType === "all") {
            groupTransactionsByDate(transactions);
        } else {
            const filtered = transactions.filter(t =>
                filterType === "income"
                    ? t.type === 'deposit'
                    : (t.type === 'withdrawal' || t.type === 'payment')
            );
            groupTransactionsByDate(filtered);
        }
    };

    const renderTransaction = ({ item }: { item: Transaction }) => {
        const isIncome = item.type === 'deposit';

        return (
            <TouchableOpacity
                className="flex-row items-center py-4 px-1"
                activeOpacity={0.7}
            >
                <View className="h-10 w-10 rounded-full justify-center items-center mr-4"
                      style={{ backgroundColor: `${isIncome ? 'rgba(52, 199, 89, 0.1)' : 'rgba(255, 59, 48, 0.1)'}` }}>
                    {getTypeIcon(item.type)}
                </View>

                <View className="flex-1 justify-between">
                    <Text className="text-lg font-medium text-[#1C1C1E] dark:text-white">
                        {getTypeLabel(item.type)}
                    </Text>
                    <Text className="text-sm text-[#8E8E93] dark:text-[#A1A1A6]">
                        {formatTime(item.date)}
                    </Text>
                </View>

                <View className="items-end">
                    <Text className={`text-lg font-semibold ${isIncome ? 'text-[#34C759]' : 'text-[#FF3B30]'}`}>
                        {isIncome ? '+' : '-'}{item.amount.toLocaleString('en-US')} PLN
                    </Text>
                    <View className="flex-row items-center mt-1">
                        <View
                            className="h-2 w-2 rounded-full mr-1"
                            style={{ backgroundColor: getStatusColor(item.status) }}
                        />
                        <Text className="text-xs text-[#8E8E93] dark:text-[#A1A1A6]">
                            {getStatusLabel(item.status)}
                        </Text>
                    </View>
                </View>
            </TouchableOpacity>
        );
    };

    const renderSectionHeader = ({ section }: { section: GroupedTransactions }) => (
        <View className="py-2 px-4 mb-1 mt-2">
            <Text className="text-[#8E8E93] dark:text-[#A1A1A6] text-sm font-medium">
                {section.title}
            </Text>
        </View>
    );

    const renderSeparator = () => (
        <View className="h-[0.5px] bg-[#E5E5EA] dark:bg-[#38383A] ml-14" />
    );

    return (
        <SafeAreaView className="flex-1 bg-[#F2F2F7] dark:bg-[#1C1C1E]">
            <StatusBar barStyle="dark-content" />

            <Stack.Screen
                options={{
                    headerShown: false
                }}
            />

            {/* Custom header */}
            <View className="pt-2 px-4 z-10">
                <Animated.View
                    className="absolute top-0 left-0 right-0 h-16"
                    style={{
                        opacity: headerOpacity,
                        backgroundColor: 'rgba(242, 242, 247, 0.9)', // Light theme
                        backdropFilter: 'blur(10px)'
                    }}
                />

                <View className="flex-row justify-between items-center mb-2 z-20">
                    <TouchableOpacity
                        onPress={() => navigation.goBack()}
                        className="w-10 h-10 items-center justify-center"
                        hitSlop={{ top: 15, right: 15, bottom: 15, left: 15 }}
                    >
                        <Ionicons name="chevron-back" size={28} color="#007AFF" />
                    </TouchableOpacity>

                    <Text className="text-[#1C1C1E] dark:text-white text-lg font-semibold">
                        Transaction History
                    </Text>

                    <TouchableOpacity
                        hitSlop={{ top: 15, right: 15, bottom: 15, left: 15 }}
                        className="w-10 h-10 items-center justify-center"
                    >
                        <Ionicons name="ellipsis-horizontal-circle" size={24} color="#007AFF" />
                    </TouchableOpacity>
                </View>
            </View>

            {/* Filters */}
            <View className="px-4 mb-2">
                <View className="flex-row rounded-xl overflow-hidden mb-2 bg-[#E5E5EA] dark:bg-[#2C2C2E]">
                    <TouchableOpacity
                        className={`flex-1 py-3 items-center ${activeFilter === 'all' ? 'bg-white dark:bg-[#3A3A3C]' : ''}`}
                        style={activeFilter === 'all' ? { shadowColor: "#000", shadowOpacity: 0.08, shadowRadius: 4, elevation: 2 } : {}}
                        onPress={() => filterTransactions('all')}
                    >
                        <Text className={`font-medium ${activeFilter === 'all' ? 'text-[#007AFF]' : 'text-[#8E8E93]'}`}>
                            All
                        </Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        className={`flex-1 py-3 items-center ${activeFilter === 'income' ? 'bg-white dark:bg-[#3A3A3C]' : ''}`}
                        style={activeFilter === 'income' ? { shadowColor: "#000", shadowOpacity: 0.08, shadowRadius: 4, elevation: 2 } : {}}
                        onPress={() => filterTransactions('income')}
                    >
                        <Text className={`font-medium ${activeFilter === 'income' ? 'text-[#007AFF]' : 'text-[#8E8E93]'}`}>
                            Income
                        </Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        className={`flex-1 py-3 items-center ${activeFilter === 'expense' ? 'bg-white dark:bg-[#3A3A3C]' : ''}`}
                        style={activeFilter === 'expense' ? { shadowColor: "#000", shadowOpacity: 0.08, shadowRadius: 4, elevation: 2 } : {}}
                        onPress={() => filterTransactions('expense')}
                    >
                        <Text className={`font-medium ${activeFilter === 'expense' ? 'text-[#007AFF]' : 'text-[#8E8E93]'}`}>
                            Expenses
                        </Text>
                    </TouchableOpacity>
                </View>
            </View>

            <View className="flex-1 px-4">
                {isLoading ? (
                    <View className="flex-1 justify-center items-center">
                        <ActivityIndicator size="large" color="#007AFF" />
                    </View>
                ) : (
                    <Animated.SectionList
                        sections={groupedTransactions}
                        keyExtractor={item => item.id}
                        renderItem={renderTransaction}
                        renderSectionHeader={renderSectionHeader}
                        ItemSeparatorComponent={renderSeparator}
                        contentContainerClassName="pb-8"
                        showsVerticalScrollIndicator={false}
                        onScroll={Animated.event(
                            [{ nativeEvent: { contentOffset: { y: scrollY } } }],
                            { useNativeDriver: true }
                        )}
                        scrollEventThrottle={16}
                        stickySectionHeadersEnabled={false}
                        ListEmptyComponent={() => (
                            <View className="flex-1 justify-center items-center py-20">
                                <View className="w-20 h-20 rounded-full bg-[#F2F2F7] dark:bg-[#2C2C2E] items-center justify-center mb-4">
                                    <Ionicons name="wallet-outline" size={40} color="#8E8E93" />
                                </View>
                                <Text className="text-[#8E8E93] dark:text-[#98989F] text-center text-base font-medium">
                                    No transactions
                                </Text>
                                <Text className="text-[#8E8E93] dark:text-[#98989F] text-center text-sm mt-1 px-10">
                                    Your transaction history will appear here
                                </Text>
                            </View>
                        )}
                    />
                )}
            </View>
        </SafeAreaView>
    );
}