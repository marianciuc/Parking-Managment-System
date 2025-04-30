import React, { useState, useEffect, useRef } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    ScrollView,
    TextInput,
    Image,
    Linking,
    ActivityIndicator,
    Alert,
    SafeAreaView,
    Pressable
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import {
    MapPin,
    Navigation,
    Star,
    MessageCircle,
    Tag,
    Car,
    ChevronLeft,
    Send,
    Clock,
    User,
    ThumbsUp
} from 'lucide-react-native';
import * as Haptics from 'expo-haptics';
import { LinearGradient } from 'expo-linear-gradient';
import Animated, {
    FadeInDown,
    FadeIn,
    SlideInRight,
    SlideInUp,
    FadeInUp,
    useAnimatedStyle,
    useSharedValue,
    withTiming,
    withSpring,
    withSequence,
    interpolate,
    Extrapolate
} from 'react-native-reanimated';
import Header from "@/app/components/header";
import * as Location from 'expo-location';

// Parking data interface
interface ParkingDetails {
    id: string;
    name: string;
    address: string;
    description: string;
    rating: number;
    totalSpots: number;
    availableSpots: number;
    tags: string[];
    coordinates: {
        latitude: number;
        longitude: number;
    };
    workingHours: string;
    pricePerHour: number;
    reviews: Review[];
}

// Review interface
interface Review {
    id: string;
    userName: string;
    rating: number;
    text: string;
    date: string;
    avatarUrl?: string;
    likes?: number;
    isLiked?: boolean;
}

const ReviewItem = ({ review, index }: { review: Review, index: number }) => {
    const [isLiked, setIsLiked] = useState(review.isLiked || false);
    const [likes, setLikes] = useState(review.likes || Math.floor(Math.random() * 10));

    const scale = useSharedValue(1);
    const animatedStyle = useAnimatedStyle(() => {
        return {
            transform: [{ scale: scale.value }]
        };
    });

    const handleLike = () => {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
        setIsLiked(!isLiked);
        setLikes(prev => isLiked ? prev - 1 : prev + 1);

        // Animation for like button
        scale.value = withSequence(
            withSpring(1.2, { damping: 2 }),
            withSpring(1, { damping: 4 })
        );
    };

    return (
        <Animated.View
            entering={SlideInRight.delay(index * 100).springify()}
            className="mb-5 p-4 bg-white rounded-xl shadow-sm border border-gray-100"
        >
            <View className="flex-row items-center mb-3">
                <View className="h-10 w-10 rounded-full bg-blue-100 items-center justify-center mr-3 overflow-hidden">
                    {review.avatarUrl ? (
                        <Image source={{ uri: review.avatarUrl }} className="h-full w-full" />
                    ) : (
                        <User size={20} color="#3b82f6" />
                    )}
                </View>
                <View className="flex-1">
                    <Text className="font-semibold text-gray-800">{review.userName}</Text>
                    <View className="flex-row items-center">
                        <View className="flex-row">
                            {[1, 2, 3, 4, 5].map(star => (
                                <Star
                                    key={star}
                                    size={12}
                                    color="#FFB800"
                                    fill={review.rating >= star ? "#FFB800" : "transparent"}
                                    strokeWidth={1.5}
                                />
                            ))}
                        </View>
                        <Text className="text-xs text-gray-500 ml-2">{review.date}</Text>
                    </View>
                </View>
            </View>
            <Text className="text-gray-700 leading-5 mb-3">{review.text}</Text>
            <View className="flex-row justify-between items-center">
                <TouchableOpacity
                    onPress={handleLike}
                    className="flex-row items-center"
                >
                    <Animated.View style={animatedStyle}>
                        <ThumbsUp
                            size={16}
                            color={isLiked ? "#3b82f6" : "#94a3b8"}
                            fill={isLiked ? "#3b82f6" : "transparent"}
                        />
                    </Animated.View>
                    <Text className={`ml-1 text-sm ${isLiked ? 'text-blue-500' : 'text-gray-500'}`}>
                        {likes}
                    </Text>
                </TouchableOpacity>
                <TouchableOpacity className="px-3 py-1 rounded-full bg-gray-50">
                    <Text className="text-xs text-gray-500">Reply</Text>
                </TouchableOpacity>
            </View>
        </Animated.View>
    );
};

const ParkingDetails = () => {
    const { parkingId } = useLocalSearchParams();
    const [parking, setParking] = useState<ParkingDetails | null>(null);
    const [loading, setLoading] = useState(true);
    const [reviewText, setReviewText] = useState('');
    const [userRating, setUserRating] = useState(0);
    const [submittingReview, setSubmittingReview] = useState(false);
    const [userLocation, setUserLocation] = useState<Location.LocationObject | null>(null);
    const [showReviewForm, setShowReviewForm] = useState(false);
    const [reviewsExpanded, setReviewsExpanded] = useState(false);

    const scrollRef = useRef<ScrollView>(null);
    const scrollOffset = useSharedValue(0);
    const headerOpacity = useSharedValue(0);
    const reviewInputAnimation = useSharedValue(0);

    const reviewInputStyle = useAnimatedStyle(() => {
        return {
            transform: [
                { translateY: interpolate(
                        reviewInputAnimation.value,
                        [0, 1],
                        [50, 0],
                        Extrapolate.CLAMP
                    )}
            ],
            opacity: reviewInputAnimation.value
        };
    });

    const router = useRouter();

    const handleScroll = (event: any) => {
        scrollOffset.value = event.nativeEvent.contentOffset.y;
        if (scrollOffset.value > 100) {
            headerOpacity.value = withTiming(1, { duration: 200 });
        } else {
            headerOpacity.value = withTiming(0, { duration: 200 });
        }
    };

    const headerStyle = useAnimatedStyle(() => {
        return {
            opacity: headerOpacity.value,
            transform: [
                { translateY: interpolate(
                        headerOpacity.value,
                        [0, 1],
                        [-10, 0],
                        Extrapolate.CLAMP
                    )}
            ]
        };
    });

    useEffect(() => {
        (async () => {
            const { status } = await Location.requestForegroundPermissionsAsync();
            if (status !== 'granted') {
                Alert.alert('Error', 'Permission to access location was not granted');
                return;
            }

            const location = await Location.getCurrentPositionAsync({});
            setUserLocation(location);
        })();
    }, []);

    useEffect(() => {
        // Server data request simulation
        setTimeout(() => {
            // Extended mock data for example
            const mockParkingData: ParkingDetails = {
                id: "w2",
                name: 'Wi ZUT Parking Spot"',
                address: 'Zolnerska, 51, Szczecin',
                description: 'Modern parking in the city center with 24/7 security and video surveillance. Convenient entry and exit, located near the business center.',
                rating: 4.7,
                totalSpots: 120,
                availableSpots: 32,
                coordinates: {
                    latitude: 55.751244,
                    longitude: 37.618423,
                },
                tags: ['Security', 'Video surveillance', 'Covered', '24/7', 'Disabled spots'],
                workingHours: '24/7',
                pricePerHour: 150,
                reviews: [
                    {
                        id: '1',
                        userName: 'Ivan Petrov',
                        rating: 5,
                        text: 'Excellent parking, always has available spots. Convenient location. Staff is polite, the area is clean. I recommend it to everyone!',
                        date: '15.05.2023',
                        avatarUrl: 'https://randomuser.me/api/portraits/men/32.jpg',
                        likes: 8
                    },
                    {
                        id: '2',
                        userName: 'Anna Sidorova',
                        rating: 4,
                        text: 'Good parking, but sometimes it can be difficult to find a spot in the evening hours. Overall, I was satisfied.',
                        date: '03.06.2023',
                        avatarUrl: 'https://randomuser.me/api/portraits/women/44.jpg',
                        likes: 3
                    },
                    {
                        id: '3',
                        userName: 'Dmitry Kozlov',
                        rating: 5,
                        text: 'Very convenient location, right in the center. Security works well, they always check cars when leaving.',
                        date: '22.07.2023',
                        likes: 12
                    },
                    {
                        id: '4',
                        userName: 'Elena Vasilyeva',
                        rating: 3,
                        text: 'Decent parking, but prices are above average. The plus is that there are spots for disabled people.',
                        date: '10.08.2023',
                        avatarUrl: 'https://randomuser.me/api/portraits/women/22.jpg',
                        likes: 1
                    }
                ]
            };

            setParking(mockParkingData);
            setLoading(false);
        }, 1500);
    }, [parkingId]);

    const handleWriteReview = () => {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
        setShowReviewForm(true);
        reviewInputAnimation.value = withSpring(1, { damping: 12 });

        setTimeout(() => {
            scrollRef.current?.scrollToEnd({ animated: true });
        }, 300);
    };

    const handleSubmitReview = () => {
        if (userRating === 0 || reviewText.trim().length === 0) {
            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
            return;
        }

        Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
        setSubmittingReview(true);

        // Review submission simulation
        setTimeout(() => {
            if (parking) {
                const newReview = {
                    id: Date.now().toString(),
                    userName: 'You',
                    rating: userRating,
                    text: reviewText,
                    date: new Date().toLocaleDateString('en-US'),
                    likes: 0,
                    isLiked: false
                };

                setParking({
                    ...parking,
                    reviews: [newReview, ...parking.reviews]
                });

                setReviewText('');
                setUserRating(0);
                setSubmittingReview(false);
                setShowReviewForm(false);
                reviewInputAnimation.value = withTiming(0, { duration: 300 });
            }
        }, 1000);
    };

    const handleStarPress = (rating: number) => {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
        setUserRating(rating);
    };

    return (
        <SafeAreaView className="flex-1 bg-white">
            {/* Floating header */}
            <Animated.View
                style={headerStyle}
                className="absolute top-0 left-0 right-0 z-10 bg-white border-b border-gray-200 px-4 py-3 flex-row items-center"
            >
                <TouchableOpacity
                    onPress={() => router.back()}
                    className="p-1 mr-3"
                >
                    <ChevronLeft size={24} color="#000" />
                </TouchableOpacity>
                <Text className="text-lg font-bold">{parking?.name || 'Parking'}</Text>
            </Animated.View>

            <ScrollView
                ref={scrollRef}
                className="flex-1"
                onScroll={handleScroll}
                scrollEventThrottle={16}
                showsVerticalScrollIndicator={false}
            >
                {loading ? (
                    <View className="flex-1 justify-center items-center pt-20">
                        <ActivityIndicator size="large" color="#3b82f6" />
                        <Animated.Text
                            entering={FadeIn.delay(300)}
                            className="mt-4 text-gray-600 font-medium"
                        >
                            Loading information...
                        </Animated.Text>
                    </View>
                ) : parking ? (
                    <View>
                        {/* Parking image */}
                        <View className="w-full h-64 bg-gray-200 relative">
                            <Image
                                source={{uri: 'https://images.unsplash.com/photo-1506521781263-d8422e82f27a?q=80&w=1170&auto=format&fit=crop'}}
                                className="w-full h-full"
                                resizeMode="cover"
                            />
                            <LinearGradient
                                colors={['rgba(0,0,0,0.5)', 'transparent']}
                                className="absolute top-0 left-0 right-0 h-20"
                            />
                            <TouchableOpacity
                                onPress={() => router.back()}
                                className="absolute top-12 left-4 bg-white/90 p-2 rounded-full"
                                style={{ elevation: 2 }}
                            >
                                <ChevronLeft size={24} color="#000" />
                            </TouchableOpacity>
                        </View>

                        {/* Name and rating */}
                        <Animated.View
                            entering={FadeInDown.delay(200).duration(500)}
                            className="px-5 pt-5"
                        >
                            <Text className="text-2xl font-bold">{parking.name}</Text>
                            <View className="flex-row items-center mt-1">
                                <View className="flex-row items-center bg-amber-50 px-2 py-1 rounded-lg">
                                    <Star size={16} color="#FFB800" fill="#FFB800" />
                                    <Text className="ml-1 font-medium text-amber-800">{parking.rating}</Text>
                                </View>
                                <Text className="ml-2 text-gray-500">• {parking.reviews.length} reviews</Text>
                            </View>
                        </Animated.View>

                        {/* Address and navigation */}
                        <Animated.View
                            entering={FadeInDown.delay(300).duration(500)}
                            className="flex-row items-center justify-between px-5 py-4"
                        >
                            <View className="flex-row items-center flex-1">
                                <MapPin size={18} color="#3b82f6" />
                                <Text className="ml-2 text-gray-700 flex-1">{parking.address}</Text>
                            </View>
                            {userLocation && (
                                <TouchableOpacity
                                    className="bg-blue-500 py-2 px-4 rounded-lg flex-row items-center"
                                    style={{ elevation: 2 }}
                                    onPress={() => {
                                        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
                                        const url = `https://maps.google.com/maps?daddr=${parking.coordinates.latitude},${parking.coordinates.longitude}`;
                                        Linking.openURL(url);
                                    }}
                                >
                                    <Navigation size={16} color="#fff" />
                                    <Text className="ml-1 text-white font-medium">Directions</Text>
                                </TouchableOpacity>
                            )}
                        </Animated.View>

                        {/* Description */}
                        <Animated.View
                            entering={FadeInDown.delay(400).duration(500)}
                            className="px-5 py-4 border-t border-gray-100"
                        >
                            <Text className="text-gray-800 leading-5">{parking.description}</Text>
                        </Animated.View>

                        {/* Availability information */}
                        <Animated.View
                            entering={FadeInDown.delay(500).duration(500)}
                            className="mx-5 my-4 p-4 bg-gray-50 rounded-xl"
                        >
                            <View className="flex-row justify-between items-center">
                                <View className="items-center">
                                    <Text className="text-gray-500 text-xs mb-1">Available</Text>
                                    <View className="flex-row items-baseline">
                                        <Text className="text-2xl font-bold text-blue-600">
                                            {parking.availableSpots}
                                        </Text>
                                        <Text className="text-sm text-gray-500 ml-1">
                                            of {parking.totalSpots}
                                        </Text>
                                    </View>
                                </View>
                                <View className="h-10 w-[1px] bg-gray-200" />
                                <View className="items-center">
                                    <Text className="text-gray-500 text-xs mb-1">Price per hour</Text>
                                    <Text className="text-2xl font-bold text-blue-600">{parking.pricePerHour} PLN</Text>
                                </View>
                                <View className="h-10 w-[1px] bg-gray-200" />
                                <View className="items-center">
                                    <Text className="text-gray-500 text-xs mb-1">Working hours</Text>
                                    <View className="flex-row items-center">
                                        <Clock size={14} color="#3b82f6" />
                                        <Text className="ml-1 font-medium text-gray-700">{parking.workingHours}</Text>
                                    </View>
                                </View>
                            </View>
                        </Animated.View>

                        {/* Tags */}
                        <Animated.View
                            entering={FadeInDown.delay(600).duration(500)}
                            className="px-5 py-4 border-t border-gray-100"
                        >
                            <Text className="font-medium mb-3">Features</Text>
                            <View className="flex-row flex-wrap">
                                {parking.tags.map((tag, index) => (
                                    <Animated.View
                                        key={index}
                                        entering={FadeIn.delay(700 + index * 100)}
                                        className="bg-blue-50 px-3 py-1.5 rounded-full mr-2 mb-2 flex-row items-center"
                                    >
                                        <Tag size={12} color="#3b82f6" />
                                        <Text className="ml-1 text-sm text-blue-700">{tag}</Text>
                                    </Animated.View>
                                ))}
                            </View>
                        </Animated.View>

                        {/* Reviews */}
                        <Animated.View
                            entering={FadeInDown.delay(700).duration(500)}
                            className="px-5 py-4 border-t border-gray-100"
                        >
                            <View className="flex-row justify-between items-center mb-4">
                                <Text className="font-semibold text-lg">Reviews</Text>
                                <TouchableOpacity
                                    onPress={handleWriteReview}
                                    className="bg-blue-50 px-3 py-1.5 rounded-lg flex-row items-center"
                                >
                                    <MessageCircle size={14} color="#3b82f6" />
                                    <Text className="ml-1 text-blue-600 font-medium">Write</Text>
                                </TouchableOpacity>
                            </View>

                            <View>
                                {(reviewsExpanded ? parking.reviews : parking.reviews.slice(0, 2)).map((review, index) => (
                                    <ReviewItem key={review.id} review={review} index={index} />
                                ))}
                            </View>

                            {parking.reviews.length > 2 && !reviewsExpanded && (
                                <TouchableOpacity
                                    onPress={() => setReviewsExpanded(true)}
                                    className="mt-2 py-3 items-center border-t border-gray-100"
                                >
                                    <Text className="text-blue-500 font-medium">Show all reviews ({parking.reviews.length})</Text>
                                </TouchableOpacity>
                            )}
                        </Animated.View>

                        {/* Review submission form */}
                        {showReviewForm && (
                            <Animated.View
                                style={reviewInputStyle}
                                className="mx-5 my-4 p-4 bg-gray-50 rounded-xl"
                            >
                                <Text className="font-medium mb-3">Write a review</Text>
                                <View className="flex-row mb-4 justify-center">
                                    {[1, 2, 3, 4, 5].map((star) => (
                                        <TouchableOpacity
                                            key={star}
                                            onPress={() => handleStarPress(star)}
                                            className="mx-2"
                                        >
                                            <Animated.View
                                                entering={userRating >= star ? FadeIn.delay(star * 100) : undefined}
                                            >
                                                <Star
                                                    size={28}
                                                    color="#FFB800"
                                                    fill={userRating >= star ? "#FFB800" : "transparent"}
                                                    strokeWidth={1.5}
                                                />
                                            </Animated.View>
                                        </TouchableOpacity>
                                    ))}
                                </View>
                                <View className="mb-4">
                                    <TextInput
                                        className="border border-gray-300 rounded-lg px-3 py-3 bg-white"
                                        placeholder="Share your experience..."
                                        value={reviewText}
                                        onChangeText={setReviewText}
                                        multiline
                                        numberOfLines={4}
                                        textAlignVertical="top"
                                    />
                                </View>
                                <View className="flex-row justify-end">
                                    <TouchableOpacity
                                        onPress={() => {
                                            setShowReviewForm(false);
                                            reviewInputAnimation.value = withTiming(0, { duration: 200 });
                                        }}
                                        className="mr-2 px-4 py-2 rounded-lg border border-gray-300"
                                    >
                                        <Text className="text-gray-700">Cancel</Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity
                                        className={`px-4 py-2 rounded-lg ${userRating > 0 && reviewText.trim().length > 0 ? 'bg-blue-500' : 'bg-gray-300'}`}
                                        disabled={userRating === 0 || reviewText.trim().length === 0 || submittingReview}
                                        onPress={handleSubmitReview}
                                    >
                                        {submittingReview ? (
                                            <ActivityIndicator size="small" color="#fff" />
                                        ) : (
                                            <Text className="text-white font-medium">Submit</Text>
                                        )}
                                    </TouchableOpacity>
                                </View>
                            </Animated.View>
                        )}

                        {/* Bottom scroll padding */}
                        <View className="h-20" />
                    </View>
                ) : (
                    <View className="flex-1 justify-center items-center p-4 pt-20">
                        <Text className="text-gray-600 text-center">Failed to load parking information</Text>
                    </View>
                )}
            </ScrollView>
        </SafeAreaView>
    );
};

export default ParkingDetails;