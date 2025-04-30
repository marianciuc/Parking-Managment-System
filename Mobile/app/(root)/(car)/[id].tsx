import {
    View,
    Text,
    TouchableOpacity,
    Modal,
    TextInput,
    ScrollView,
    Alert,
    ActivityIndicator,
    Dimensions,
    StyleSheet,
    SafeAreaView
} from 'react-native'
import React, { useContext, useEffect, useState, useMemo } from 'react'
import Header from "@/app/components/header"
import IVehicle from "../../interfaces/IVehicle"
import { useLocalSearchParams, useRouter } from 'expo-router'
import { Context } from "@/app/_layout"
import { Clock, Info, Trash2, X, Calendar, Car, Tag, Edit2, ArrowLeft, CheckCircle2, AlertCircle, Shield, Gauge, Fuel } from "lucide-react-native"
import * as Haptics from 'expo-haptics'
import { Picker } from '@react-native-picker/picker'
import { LinearGradient } from 'expo-linear-gradient'
import Animated, { FadeInDown, FadeInRight, FadeIn, useAnimatedStyle, useSharedValue, withTiming } from 'react-native-reanimated'
import { BlurView } from 'expo-blur'

const vehicleTypes = [
    'Седан', 'Хэтчбек', 'Универсал', 'Внедорожник', 'Кроссовер',
    'Минивэн', 'Купе', 'Кабриолет', 'Пикап', 'Микроавтобус', 'Другое'
]

interface EditableVehicleData {
    brand: string
    model: string
    year: string
    type: string
}

interface StatData {
    title: string
    value: string
    description: string
    icon: React.ReactNode
    color: string
}

const windowWidth = Dimensions.get('window').width

const CarDetails = () => {
    const { vehicleStore } = useContext(Context)
    const { id } = useLocalSearchParams()
    const [vehicle, setVehicle] = useState<IVehicle | undefined>()
    const [editModalVisible, setEditModalVisible] = useState(false)
    const [deleteConfirmVisible, setDeleteConfirmVisible] = useState(false)
    const [editData, setEditData] = useState<EditableVehicleData>({
        brand: '',
        model: '',
        year: '',
        type: '',
    })
    const [saving, setSaving] = useState(false)
    const [deleteLoading, setDeleteLoading] = useState(false)
    const [activeTab, setActiveTab] = useState('info')

    const scrollOffset = useSharedValue(0)
    const headerOpacity = useSharedValue(0)

    const router = useRouter()

    useEffect(() => {
        const vehicleData = vehicleStore.vehiclePage.find(vehicle => vehicle.id === id)
        if (vehicleData) {
            setVehicle(vehicleData)
            setEditData({
                brand: vehicleData.brand || '',
                model: vehicleData.model || '',
                year: vehicleData.year?.toString() || '',
                type: vehicleData.type || 'Sedan',
            })
        }
    }, [id, vehicleStore.vehiclePage])

    const handleScroll = (event: { nativeEvent: { contentOffset: { y: number } } }) => {
        const y = event.nativeEvent.contentOffset.y
        scrollOffset.value = y
        headerOpacity.value = withTiming(y > 120 ? 1 : 0, { duration: 200 })
    }

    const headerAnimStyle = useAnimatedStyle(() => {
        return {
            opacity: headerOpacity.value,
        }
    })

    const handleEditPress = () => {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium)
        setEditModalVisible(true)
    }

    const handleDeletePress = () => {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium)
        setDeleteConfirmVisible(true)
    }

    const handleSaveChanges = async () => {
        if (!vehicle || !editData.brand || !editData.model) {
            Alert.alert('Error', 'Fill required fields')
            return
        }

        setSaving(true)

        try {
            const updatedVehicle = {
                ...vehicle,
                brand: editData.brand,
                model: editData.model,
                year: editData.year ? parseInt(editData.year) : null,
                type: editData.type,
            }

            // Здесь должен быть вызов метода для обновления данных автомобиля
            // await vehicleStore.updateVehicle(updatedVehicle)

            // Обновляем локальное состояние
            setVehicle(updatedVehicle)
            setEditModalVisible(false)

            // Тактильный отклик при успешном сохранении
            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success)
            Alert.alert('Saved', 'Successful updated')
        } catch (error) {
            console.error('Error when updating the vehicle:', error)
            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error)
            Alert.alert('Error', 'Sorry, something went wrong')
        } finally {
            setSaving(false)
        }
    }

    const handleDeleteVehicle = async () => {
        if (!vehicle) return

        setDeleteLoading(true)
        try {
            // Здесь должен быть вызов метода для удаления автомобиля
            // await vehicleStore.deleteVehicle(vehicle.id)

            setDeleteConfirmVisible(false)
            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success)
            Alert.alert('Deleted', 'Vehicle was deleted ')
            router.back()
        } catch (error) {
            console.error('Error when deleting vehicle:', error)
            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error)
            Alert.alert('Error', 'Vehicle was not deleted')
        } finally {
            setDeleteLoading(false)
        }
    }

    // Статистика использования автомобиля (примерные данные)
    const statsData: StatData[] = useMemo(() => [
        {
            title: 'Time on parking',
            value: '223h 32min',
            description: 'Vehicle parking time',
            icon: <Clock size={22} color="#fff" />,
            color: '#3B82F6',
        },
        {
            title: 'Usage',
            value: '45 days',
            description: 'Active usage',
            icon: <Car size={22} color="#fff" />,
            color: '#10B981',
        },
        {
            title: 'Spent',
            value: '252.42 PLN',
            description: 'Money spent',
            icon: <Shield size={22} color="#fff" />,
            color: '#8B5CF6',
        },
    ], [])

    // Генерация цветов для градиента на основе типа автомобиля
    const getGradientColors = () => {
        const typeColorMap: Record<string, string[]> = {
            'Седан': ['#3B82F6', '#2563EB'],
            'Хэтчбек': ['#8B5CF6', '#7C3AED'],
            'Универсал': ['#EC4899', '#DB2777'],
            'Внедорожник': ['#F59E0B', '#D97706'],
            'Кроссовер': ['#10B981', '#059669'],
            'Минивэн': ['#6366F1', '#4F46E5'],
            'Купе': ['#EF4444', '#DC2626'],
            'Кабриолет': ['#F97316', '#EA580C'],
            'Пикап': ['#84CC16', '#65A30D'],
            'Микроавтобус': ['#06B6D4', '#0891B2'],
            'Другое': ['#6B7280', '#4B5563'],
        }

        return typeColorMap[vehicle?.type || 'Другое'] || ['#3B82F6', '#2563EB']
    }

    return (
        <SafeAreaView className="flex-1 bg-white">
        <View style={styles.container}>
            <Header header="Vehicle Profile" style="header" textColor="white" />

            {/* Анимированный заголовок при скролле */}
            <Animated.View style={[styles.animatedHeader, headerAnimStyle]}>
                <BlurView intensity={80} tint="dark" style={styles.blurView}>
                    <Text style={styles.animatedHeaderText}>
                        {vehicle?.brand} {vehicle?.model}
                    </Text>
                </BlurView>
            </Animated.View>

            <ScrollView
                style={styles.scrollView}
                showsVerticalScrollIndicator={false}
                onScroll={handleScroll}
                scrollEventThrottle={16}
            >
                {/* Градиентная карточка автомобиля вместо изображения */}
                <Animated.View
                    entering={FadeInDown.duration(400).delay(100)}
                    style={styles.vehicleCardContainer}
                >
                    <LinearGradient
                        colors={getGradientColors()}
                        start={{ x: 0, y: 0 }}
                        end={{ x: 1, y: 1 }}
                        style={styles.vehicleCard}
                    >
                        <View style={styles.vehicleCardContent}>
                            <View>
                                <Text style={styles.vehicleCardBrand}>
                                    {vehicle?.brand}
                                </Text>
                                <Text style={styles.vehicleCardModel}>
                                    {vehicle?.model}
                                </Text>
                                <Text style={styles.vehicleCardYear}>
                                    {vehicle?.year || 'Year not specified'}
                                </Text>
                            </View>
                            <View style={styles.plateContainer}>
                                <Text style={styles.plateNumber}>
                                    {vehicle?.plateNumber}
                                </Text>
                            </View>
                        </View>
                        <View style={styles.vehicleTypeTag}>
                            <Car size={14} color="#fff" />
                            <Text style={styles.vehicleTypeText}>
                                {vehicle?.type || 'Type not specified'}
                            </Text>
                        </View>
                    </LinearGradient>
                </Animated.View>

                {/* Кнопки действий */}
                <Animated.View
                    entering={FadeInDown.duration(400).delay(200)}
                    style={styles.actionButtons}
                >
                    <TouchableOpacity
                        style={styles.editButton}
                        onPress={handleEditPress}
                    >
                        <Edit2 size={18} color="#fff" />
                        <Text style={styles.buttonText}>Edit</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        style={styles.deleteButton}
                        onPress={handleDeletePress}
                    >
                        <Trash2 size={18} color="#fff" />
                        <Text style={styles.buttonText}>Delete</Text>
                    </TouchableOpacity>
                </Animated.View>

                {/* Статистика использования */}
                <Animated.View
                    entering={FadeInDown.duration(400).delay(300)}
                    style={styles.statsContainer}
                >
                    <ScrollView
                        horizontal
                        showsHorizontalScrollIndicator={false}
                        contentContainerStyle={styles.statsScrollView}
                    >
                        {statsData.map((stat, index) => (
                            <Animated.View
                                key={index}
                                entering={FadeInRight.duration(400).delay(400 + index * 100)}
                                style={styles.statCard}
                            >
                                <LinearGradient
                                    colors={[stat.color, stat.color + 'D9']}
                                    start={{ x: 0, y: 0 }}
                                    end={{ x: 1, y: 1 }}
                                    style={styles.statGradient}
                                >
                                    <View style={styles.statIconContainer}>
                                        {stat.icon}
                                    </View>
                                    <Text style={styles.statValue}>{stat.value}</Text>
                                    <Text style={styles.statDescription}>{stat.description}</Text>
                                </LinearGradient>
                            </Animated.View>
                        ))}
                    </ScrollView>
                </Animated.View>

                {/* Переключатель вкладок */}
                <Animated.View
                    entering={FadeInDown.duration(400).delay(400)}
                    style={styles.tabContainer}
                >
                    <TouchableOpacity
                        style={[
                            styles.tabButton,
                            activeTab === 'info' && styles.activeTabButton
                        ]}
                        onPress={() => setActiveTab('info')}
                    >
                        <Info size={16} color={activeTab === 'info' ? '#3B82F6' : '#64748B'} />
                        <Text style={[
                            styles.tabButtonText,
                            activeTab === 'info' && styles.activeTabText
                        ]}>
                            Information
                        </Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        style={[
                            styles.tabButton,
                            activeTab === 'stats' && styles.activeTabButton
                        ]}
                        onPress={() => setActiveTab('stats')}
                    >
                        <Gauge size={16} color={activeTab === 'stats' ? '#3B82F6' : '#64748B'} />
                        <Text style={[
                            styles.tabButtonText,
                            activeTab === 'stats' && styles.activeTabText
                        ]}>
                            Statistic
                        </Text>
                    </TouchableOpacity>
                </Animated.View>

                {/* Контент вкладки "Информация" */}
                {activeTab === 'info' && (
                    <Animated.View
                        entering={FadeIn.duration(400)}
                        style={styles.infoContainer}
                    >
                        <Text style={styles.sectionTitle}>Main information</Text>

                        <View style={styles.infoItem}>
                            <Car size={20} color="#3B82F6" />
                            <View style={styles.infoContent}>
                                <Text style={styles.infoLabel}>Brand and model</Text>
                                <Text style={styles.infoValue}>{vehicle?.brand} {vehicle?.model}</Text>
                            </View>
                        </View>

                        <View style={styles.infoItem}>
                            <Calendar size={20} color="#3B82F6" />
                            <View style={styles.infoContent}>
                                <Text style={styles.infoLabel}>Year of production</Text>
                                <Text style={styles.infoValue}>{vehicle?.year || 'Not specified'}</Text>
                            </View>
                        </View>

                        <View style={styles.infoItem}>
                            <Tag size={20} color="#3B82F6" />
                            <View style={styles.infoContent}>
                                <Text style={styles.infoLabel}>Plate number</Text>
                                <Text style={styles.infoValue}>{vehicle?.plateNumber}</Text>
                            </View>
                        </View>

                        <View style={styles.infoItem}>
                            <Fuel size={20} color="#3B82F6" />
                            <View style={styles.infoContent}>
                                <Text style={styles.infoLabel}>Vehicle type</Text>
                                <Text style={styles.infoValue}>{vehicle?.type || 'No data'}</Text>
                            </View>
                        </View>

                        <View style={styles.addInfoButton}>
                            <TouchableOpacity style={styles.addInfoButtonInner}>
                                <Text style={styles.addInfoButtonText}>Add information</Text>
                            </TouchableOpacity>
                        </View>
                    </Animated.View>
                )}

                {/* Контент вкладки "Статистика" */}
                {activeTab === 'stats' && (
                    <Animated.View
                        entering={FadeIn.duration(400)}
                        style={styles.statsContentContainer}
                    >
                        <Text style={styles.sectionTitle}>Usage statistics</Text>

                        <View style={styles.statsCard}>
                            <View style={styles.statsHeader}>
                                <Text style={styles.statsTitle}>Parking history</Text>
                                <TouchableOpacity>
                                    <Text style={styles.seeAllButton}>Show all</Text>
                                </TouchableOpacity>
                            </View>

                            <View style={styles.parkingHistoryItem}>
                                <View style={styles.parkingHistoryLeft}>
                                    <View style={[styles.statusIndicator, { backgroundColor: '#10B981' }]} />
                                    <View>
                                        <Text style={styles.parkingDate}>1 april 2025</Text>
                                        <Text style={styles.parkingTime}>18:30 - 18:45</Text>
                                    </View>
                                </View>
                                <Text style={styles.parkingDuration}>15min</Text>
                            </View>

                            <View style={styles.parkingHistoryItem}>
                                <View style={styles.parkingHistoryLeft}>
                                    <View style={[styles.statusIndicator, { backgroundColor: '#10B981' }]} />
                                    <View>
                                        <Text style={styles.parkingDate}>1 april 2025</Text>
                                        <Text style={styles.parkingTime}>09:15 - 17:30</Text>
                                    </View>
                                </View>
                                <Text style={styles.parkingDuration}>8h 15min</Text>
                            </View>

                            <View style={styles.parkingHistoryItem}>
                                <View style={styles.parkingHistoryLeft}>
                                    <View style={[styles.statusIndicator, { backgroundColor: '#F59E0B' }]} />
                                    <View>
                                        <Text style={styles.parkingDate}>2 may 2025</Text>
                                        <Text style={styles.parkingTime}>11:00 - 15:30</Text>
                                    </View>
                                </View>
                                <Text style={styles.parkingDuration}>4h 30min</Text>
                            </View>
                        </View>

                        <View style={styles.usageSummary}>
                            <Text style={styles.usageSummaryTitle}>Money spent</Text>
                            <Text style={styles.usageSummaryValue}>12,560</Text>
                            <Text style={styles.usageSummaryDescription}>
                                By using the service, you have saved money on parking
                            </Text>
                        </View>
                    </Animated.View>
                )}

                <View style={styles.spacer} />
            </ScrollView>

            {/* Модальное окно редактирования */}
            <Modal
                visible={editModalVisible}
                animationType="slide"
                transparent={true}
                onRequestClose={() => setEditModalVisible(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContent}>
                        <View style={styles.modalHeader}>
                            <Text style={styles.modalTitle}>Editing the vehicle</Text>
                            <TouchableOpacity
                                style={styles.closeButton}
                                onPress={() => setEditModalVisible(false)}
                            >
                                <X size={22} color="#64748B" />
                            </TouchableOpacity>
                        </View>

                        <ScrollView style={styles.modalScrollView}>
                            <View style={styles.formGroup}>
                                <Text style={styles.formLabel}>Brand</Text>
                                <TextInput
                                    style={styles.formInput}
                                    value={editData.brand}
                                    onChangeText={(text) => setEditData({...editData, brand: text})}
                                    placeholder="Provide brand of the vehicle"
                                />
                            </View>

                            <View style={styles.formGroup}>
                                <Text style={styles.formLabel}>Модель</Text>
                                <TextInput
                                    style={styles.formInput}
                                    value={editData.model}
                                    onChangeText={(text) => setEditData({...editData, model: text})}
                                    placeholder="Provide model of the vehicle"
                                />
                            </View>

                            <View style={styles.formGroup}>
                                <Text style={styles.formLabel}>Year of production</Text>
                                <TextInput
                                    style={styles.formInput}
                                    value={editData.year}
                                    onChangeText={(text) => {
                                        if (/^\d*$/.test(text)) {
                                            setEditData({...editData, year: text});
                                        }
                                    }}
                                    placeholder="Enter year of production"
                                    keyboardType="numeric"
                                    maxLength={4}
                                />
                            </View>

                            <View style={styles.formGroup}>
                                <Text style={styles.formLabel}>Vehicle type</Text>
                                <View style={styles.pickerContainer}>
                                    <Picker
                                        selectedValue={editData.type}
                                        onValueChange={(value) => setEditData({...editData, type: value})}
                                        style={styles.picker}
                                    >
                                        {vehicleTypes.map((type, index) => (
                                            <Picker.Item key={index} label={type} value={type} />
                                        ))}
                                    </Picker>
                                </View>
                            </View>

                            <View style={styles.formGroup}>
                                <Text style={styles.formLabel}>Plate number</Text>
                                <View style={styles.disabledInputContainer}>
                                    <Text style={styles.disabledInputText}>{vehicle?.plateNumber}</Text>
                                    <Text style={styles.disabledInputNote}>Yo can't change this information</Text>
                                </View>
                            </View>

                            <TouchableOpacity
                                style={[styles.saveButton, saving && styles.savingButton]}
                                onPress={handleSaveChanges}
                                disabled={saving}
                            >
                                {saving ? (
                                    <ActivityIndicator size="small" color="#fff" />
                                ) : (
                                    <>
                                        <CheckCircle2 size={20} color="#fff" />
                                        <Text style={styles.saveButtonText}>Save changes</Text>
                                    </>
                                )}
                            </TouchableOpacity>
                        </ScrollView>
                    </View>
                </View>
            </Modal>

            {/* Модальное окно подтверждения удаления */}
            <Modal
                visible={deleteConfirmVisible}
                animationType="fade"
                transparent={true}
                onRequestClose={() => setDeleteConfirmVisible(false)}
            >
                <View style={styles.deleteModalOverlay}>
                    <View style={styles.deleteModalContent}>
                        <View style={styles.deleteIconContainer}>
                            <AlertCircle size={40} color="#EF4444" />
                        </View>
                        <Text style={styles.deleteModalTitle}>Vehicle deleting</Text>
                        <Text style={styles.deleteModalText}>
                            Are you sure you want to delete the car {vehicle?.brand} {vehicle?.model}? This action cannot be undone.
                        </Text>

                        <View style={styles.deleteModalButtons}>
                            <TouchableOpacity
                                style={styles.cancelButton}
                                onPress={() => setDeleteConfirmVisible(false)}
                                disabled={deleteLoading}
                            >
                                <Text style={styles.cancelButtonText}>Cancel</Text>
                            </TouchableOpacity>

                            <TouchableOpacity
                                style={styles.confirmDeleteButton}
                                onPress={handleDeleteVehicle}
                                disabled={deleteLoading}
                            >
                                {deleteLoading ? (
                                    <ActivityIndicator size="small" color="#fff" />
                                ) : (
                                    <Text style={styles.confirmDeleteText}>Delete</Text>
                                )}
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        </View>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F8FAFC',
    },
    scrollView: {
        flex: 1,
    },
    animatedHeader: {
        position: 'absolute',
        top: 50,
        left: 0,
        right: 0,
        zIndex: 100,
    },
    blurView: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 12,
    },
    animatedHeaderText: {
        fontFamily: 'poppins-semibold',
        fontSize: 16,
        color: '#fff',
    },
    vehicleCardContainer: {
        margin: 16,
        borderRadius: 16,
        overflow: 'hidden',
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 4},
        shadowOpacity: 0.1,
        shadowRadius: 8,
    },
    vehicleCard: {
        padding: 20,
        height: 180,
    },
    vehicleCardContent: {
        flex: 1,
        justifyContent: 'space-between',
    },
    vehicleCardBrand: {
        fontFamily: 'poppins-semibold',
        fontSize: 24,
        color: '#fff',
    },
    vehicleCardModel: {
        fontFamily: 'poppins-medium',
        fontSize: 20,
        color: '#fff',
        opacity: 0.9,
    },
    vehicleCardYear: {
        fontFamily: 'poppins-regular',
        fontSize: 16,
        color: '#fff',
        opacity: 0.8,
        marginTop: 4,
    },
    plateContainer: {
        alignSelf: 'flex-start',
        backgroundColor: 'rgba(255,255,255,0.2)',
        paddingHorizontal: 12,
        paddingVertical: 6,
        borderRadius: 8,
        marginTop: 10,
    },
    plateNumber: {
        fontFamily: 'poppins-semibold',
        fontSize: 18,
        color: '#fff',
    },
    vehicleTypeTag: {
        position: 'absolute',
        top: 16,
        right: 16,
        backgroundColor: 'rgba(0,0,0,0.3)',
        flexDirection: 'row',
        alignItems: 'center',
        paddingHorizontal: 10,
        paddingVertical: 4,
        borderRadius: 12,
    },
    vehicleTypeText: {
        color: '#fff',
        fontFamily: 'poppins-medium',
        fontSize: 12,
        marginLeft: 4,
    },
    actionButtons: {
        flexDirection: 'row',
        paddingHorizontal: 16,
        marginBottom: 16,
    },
    editButton: {
        flex: 1,
        backgroundColor: '#3B82F6',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 12,
        borderRadius: 12,
        marginRight: 8,
    },
    deleteButton: {
        backgroundColor: '#EF4444',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 12,
        paddingHorizontal: 16,
        borderRadius: 12,
    },
    buttonText: {
        color: '#fff',
        fontFamily: 'poppins-medium',
        fontSize: 14,
        marginLeft: 8,
    },
    statsContainer: {
        marginBottom: 16,
    },
    statsScrollView: {
        paddingHorizontal: 16,
    },
    statCard: {
        width: windowWidth * 0.7,
        height: 120,
        marginRight: 12,
        borderRadius: 16,
        overflow: 'hidden',
    },
    statGradient: {
        flex: 1,
        padding: 16,
    },
    statIconContainer: {
        width: 40,
        height: 40,
        borderRadius: 20,
        backgroundColor: 'rgba(255,255,255,0.3)',
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 12,
    },
    statValue: {
        fontFamily: 'poppins-semibold',
        fontSize: 20,
        color: '#fff',
    },
    statDescription: {
        fontFamily: 'poppins-regular',
        fontSize: 14,
        color: '#fff',
        opacity: 0.8,
    },
    tabContainer: {
        flexDirection: 'row',
        marginHorizontal: 16,
        marginBottom: 16,
        backgroundColor: '#E2E8F0',
        borderRadius: 12,
        padding: 4,
    },
    tabButton: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 10,
        borderRadius: 8,
    },
    activeTabButton: {
        backgroundColor: '#fff',
    },
    tabButtonText: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#64748B',
        marginLeft: 6,
    },
    activeTabText: {
        color: '#3B82F6',
    },
    infoContainer: {
        marginHorizontal: 16,
        backgroundColor: '#fff',
        borderRadius: 16,
        padding: 16,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.05,
        shadowRadius: 8,
        elevation: 2,
        marginBottom: 16,
    },
    sectionTitle: {
        fontFamily: 'poppins-semibold',
        fontSize: 18,
        color: '#1E293B',
        marginBottom: 16,
    },
    infoItem: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#F1F5F9',
    },
    infoContent: {
        marginLeft: 12,
        flex: 1,
    },
    infoLabel: {
        fontFamily: 'poppins-regular',
        fontSize: 14,
        color: '#64748B',
    },
    infoValue: {
        fontFamily: 'poppins-medium',
        fontSize: 16,
        color: '#1E293B',
    },
    addInfoButton: {
        marginTop: 16,
    },
    addInfoButtonInner: {
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 12,
        borderWidth: 1,
        borderColor: '#3B82F6',
        borderRadius: 8,
        borderStyle: 'dashed',
    },
    addInfoButtonText: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#3B82F6',
    },
    statsContentContainer: {
        marginHorizontal: 16,
    },
    statsCard: {
        backgroundColor: '#fff',
        borderRadius: 16,
        padding: 16,
        marginBottom: 16,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.05,
        shadowRadius: 8,
        elevation: 2,
    },
    statsHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 16,
    },
    statsTitle: {
        fontFamily: 'poppins-semibold',
        fontSize: 16,
        color: '#1E293B',
    },
    seeAllButton: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#3B82F6',
    },
    parkingHistoryItem: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#F1F5F9',
    },
    parkingHistoryLeft: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    statusIndicator: {
        width: 10,
        height: 10,
        borderRadius: 5,
        marginRight: 12,
    },
    parkingDate: {
        fontFamily: 'poppins-medium',
        fontSize: 15,
        color: '#1E293B',
    },
    parkingTime: {
        fontFamily: 'poppins-regular',
        fontSize: 13,
        color: '#64748B',
    },
    parkingDuration: {
        fontFamily: 'poppins-semibold',
        fontSize: 14,
        color: '#3B82F6',
    },
    usageSummary: {
        backgroundColor: '#fff',
        borderRadius: 16,
        padding: 20,
        alignItems: 'center',
        marginBottom: 16,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.05,
        shadowRadius: 8,
        elevation: 2,
    },
    usageSummaryTitle: {
        fontFamily: 'poppins-medium',
        fontSize: 16,
        color: '#64748B',
        marginBottom: 8,
    },
    usageSummaryValue: {
        fontFamily: 'poppins-bold',
        fontSize: 28,
        color: '#10B981',
        marginBottom: 8,
    },
    usageSummaryDescription: {
        fontFamily: 'poppins-regular',
        fontSize: 14,
        color: '#64748B',
        textAlign: 'center',
    },
    spacer: {
        height: 40,
    },
    modalOverlay: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.5)',
        justifyContent: 'flex-end',
    },
    modalContent: {
        backgroundColor: '#fff',
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
        maxHeight: '90%',
    },
    modalHeader: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: 16,
        borderBottomWidth: 1,
        borderBottomColor: '#F1F5F9',
    },
    modalTitle: {
        fontFamily: 'poppins-semibold',
        fontSize: 18,
        color: '#1E293B',
    },
    closeButton: {
        padding: 4,
    },
    modalScrollView: {
        padding: 16,
    },
    formGroup: {
        marginBottom: 16,
    },
    formLabel: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#64748B',
        marginBottom: 8,
    },
    formInput: {
        fontFamily: 'poppins-regular',
        fontSize: 16,
        color: '#1E293B',
        backgroundColor: '#F8FAFC',
        borderRadius: 8,
        paddingHorizontal: 12,
        paddingVertical: 10,
        borderWidth: 1,
        borderColor: '#E2E8F0',
    },
    pickerContainer: {
        borderWidth: 1,
        borderColor: '#E2E8F0',
        borderRadius: 8,
        backgroundColor: '#F8FAFC',
        overflow: 'hidden',
    },
    picker: {
        height: 50,
    },
    disabledInputContainer: {
        backgroundColor: '#F1F5F9',
        borderRadius: 8,
        paddingHorizontal: 12,
        paddingVertical: 10,
        borderWidth: 1,
        borderColor: '#E2E8F0',
    },
    disabledInputText: {
        fontFamily: 'poppins-medium',
        fontSize: 16,
        color: '#64748B',
    },
    disabledInputNote: {
        fontFamily: 'poppins-regular',
        fontSize: 12,
        color: '#94A3B8',
        marginTop: 4,
    },
    saveButton: {
        backgroundColor: '#3B82F6',
        borderRadius: 8,
        paddingVertical: 12,
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'row',
        marginTop: 16,
    },
    savingButton: {
        backgroundColor: '#60A5FA',
    },
    saveButtonText: {
        fontFamily: 'poppins-semibold',
        fontSize: 16,
        color: '#fff',
        marginLeft: 8,
    },
    deleteModalOverlay: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.5)',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 16,
    },
    deleteModalContent: {
        backgroundColor: '#fff',
        borderRadius: 16,
        padding: 24,
        width: '100%',
        maxWidth: 340,
        alignItems: 'center',
    },
    deleteIconContainer: {
        marginBottom: 16,
    },
    deleteModalTitle: {
        fontFamily: 'poppins-semibold',
        fontSize: 20,
        color: '#1E293B',
        marginBottom: 8,
    },
    deleteModalText: {
        fontFamily: 'poppins-regular',
        fontSize: 14,
        color: '#64748B',
        textAlign: 'center',
        marginBottom: 24,
    },
    deleteModalButtons: {
        flexDirection: 'row',
        width: '100%',
    },
    cancelButton: {
        flex: 1,
        paddingVertical: 12,
        alignItems: 'center',
        justifyContent: 'center',
        borderWidth: 1,
        borderColor: '#CBD5E1',
        borderRadius: 8,
        marginRight: 8,
    },
    cancelButtonText: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#64748B',
    },
    confirmDeleteButton: {
        flex: 1,
        backgroundColor: '#EF4444',
        paddingVertical: 12,
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 8,
        marginLeft: 8,
    },
    confirmDeleteText: {
        fontFamily: 'poppins-medium',
        fontSize: 14,
        color: '#fff',
    }
});

export default CarDetails;

