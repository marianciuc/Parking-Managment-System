"use client"

//forms logic
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"
import {REGEXP_ONLY_DIGITS} from "input-otp"



//forms ui components
import {Button} from "@/components/ui/button"
import {
    Form,
    FormControl,
    // FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import {Input} from "@/components/ui/input"
import {
    InputOTP,
    InputOTPGroup,
    // InputOTPSeparator,
    InputOTPSlot,
} from "@/components/ui/input-otp"
import { Checkbox } from "@/components/ui/checkbox"

//popovers
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command"

//icons
import {Check, ChevronsUpDown} from "lucide-react"


import {cn} from "@/lib/utils"

//react
import  {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";

//api calls
import axios from "axios";
// import ParkingService from "@/services/ParkingService.ts";

//google maps
import LocationMap from "@/components/maps/locationMap.tsx";
import {countries} from "@/interfaces/StaticData.ts";
import {ILocation, IMarkerPosition, IQueryParts, IZoomLevels} from "@/models/common/googleMaps";


//application store
import {Context} from "@/main.tsx";


//dto
import{CreateParkingStep1Values} from "@/interfaces/Interfaces.ts";
import {SCreateParkingStep1} from "@/models/common/formSchemas";



const GOOGLE_MAPS_API_KEY = import.meta.env.VITE_REACT_APP_GOOGLE_MAPS_API_KEY as string;



function CreateParkingStep1() {


    const {parkingStore} = useContext(Context);
    const form = useForm<z.infer<typeof SCreateParkingStep1>>({
        resolver: zodResolver(SCreateParkingStep1),
        defaultValues: {
            name: "",
            country: "",
            city: "",
            postalCode: "",
            street: "",
            houseNumber: "",
            belongToInstitution: false,
            institutionName: "",
            termsAccept: false,
        },
        mode: "onChange",
    })
    const {watch} = form;


    //google map view position
    const [location, setLocation] = useState<ILocation>({
        lat: null,
        lng: null,
        zoom: 2,
    })

    const [markerPosition, setMarkerPosition] = useState<IMarkerPosition>({
        lat: null,
        lng: null,
    });

    const isFormValid = form.formState.isValid && (markerPosition !== null) && watch("termsAccept") === true;




    //google maps
    function getCountryNameByCode(code: string | undefined) {
        const country = countries.find(country => country.value === code);
        return country ? country.label : undefined;
    }



    const handleLocationChange = async (field:string, value:string | undefined , formValues:CreateParkingStep1Values) => {
        try {
            console.log(formValues);
            // Build the query string progressively
            const queryParts : IQueryParts = {
                country: getCountryNameByCode(formValues.country) || "",
                city: formValues.city || "",
                postalCode: formValues.postalCode || "",
                street: formValues.street || "",
                houseNumber: formValues.houseNumber || "",
            };

            // Update the specific field
            queryParts[field as keyof IQueryParts] = value;

            // Construct the address string
            const query = `${queryParts.houseNumber} ${queryParts.street}, ${queryParts.postalCode} ${queryParts.city}, ${queryParts.country}`
                .trim()
                .replace(/\s+/g, " ");

            console.log(query);

            // Fetch coordinates using the Google Geocoding API
            const response = await axios.get(
                `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(
                    query
                )}&key=${GOOGLE_MAPS_API_KEY}`
            );

            if (response.data.results.length) {
                const {lat, lng} = response.data.results[0].geometry.location;
                const zoomLevels : IZoomLevels = {
                    country: 5,
                    city: 8,
                    postalCode: 11,
                    street: 14,
                    houseNumber: 17,
                };

                setLocation({
                    lat,
                    lng,
                    zoom: zoomLevels[field as keyof IZoomLevels] || 2,
                });
            } else {
                console.warn("No results found for the given address");
            }
        } catch (error) {
            console.error("Error fetching location data:", error);
        }
    };

    const navigate = useNavigate();



    function onSubmit(values: z.infer<typeof SCreateParkingStep1>) {
        // Do something with the form values.
        // ✅ This will be type-safe and validated.

            console.log(values.houseNumber)
            console.log(values.street)
            parkingStore.createParking({
                name: values.name,
                address: {
                    id: null,
                    recordStatus: null,
                    creationDate: null,
                    modificationDate: null,
                    countryCode: values.country,
                    city: values.city,
                    postalCode: values.postalCode,
                    street: values.street,
                    buildingNumber: values.houseNumber,
                    latitude: location.lat,
                    longitude: location.lng,
                    isBelongToAnyInstitution: values.belongToInstitution ?? false,
                    institutionName: values.institutionName,
                }
            }).then(response => {
                console.log(response);
                const parkingId = localStorage.getItem("active-parking");
                navigate(`/parking/${parkingId}/settings/general`);

            }).catch(error => {
                console.log(error);
            }).finally(()=>{
                console.log("finally")
            })

        console.log(values)
        console.log(markerPosition)
    }


    return (
        <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto">

            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 ">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="name"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Parking name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide parking name here" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="country"
                            render={({field}) => (
                                <FormItem className="text-left flex flex-col">
                                    <FormLabel>Country</FormLabel>
                                    <Popover>
                                        <PopoverTrigger asChild>
                                            <FormControl>
                                                <Button
                                                    variant="outline"
                                                    role="combobox"
                                                    className={cn(
                                                        "w-full justify-between",
                                                        !field.value && "text-muted-foreground"
                                                    )}
                                                >
                                                    {field.value
                                                        ? countries.find(
                                                            (country) => country.value === field.value
                                                        )?.label
                                                        : "Select country"}
                                                    <ChevronsUpDown className="opacity-50"/>
                                                </Button>
                                            </FormControl>
                                        </PopoverTrigger>
                                        <PopoverContent className="w-[200px] p-0">
                                            <Command>
                                                <CommandInput
                                                    placeholder="Search country..."
                                                    className="h-9"

                                                />
                                                <CommandList>
                                                    <CommandEmpty>No country found.</CommandEmpty>
                                                    <CommandGroup>
                                                        {countries.map((country) => (
                                                            <CommandItem
                                                                value={country.label}
                                                                key={country.value}
                                                                onSelect={() => {
                                                                    form.setValue("country", country.value)

                                                                    handleLocationChange("country", getCountryNameByCode(country.value), form.getValues());

                                                                }}
                                                            >
                                                                {country.label}
                                                                <Check
                                                                    className={cn(
                                                                        "ml-auto",
                                                                        country.value === field.value
                                                                            ? "opacity-100"
                                                                            : "opacity-0"
                                                                    )}
                                                                />
                                                            </CommandItem>
                                                        ))}
                                                    </CommandGroup>
                                                </CommandList>
                                            </Command>
                                        </PopoverContent>
                                    </Popover>
                                </FormItem>
                            )}
                        />


                        <FormField
                            control={form.control}
                            name="city"
                            render={({field}) => (
                                <FormItem className="text-left w-full">
                                    <FormLabel>City</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your parking city"
                                               {...field}
                                               onChange={(e) => {
                                                   field.onChange(e);
                                                   handleLocationChange("city", e.target.value, form.getValues());
                                               }}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="postalCode"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Postal Code</FormLabel>
                                    <FormControl>
                                        <InputOTP maxLength={5}{...field}
                                                  onChange={(e) => {
                                                      field.onChange(e);
                                                      handleLocationChange("postalCode", e, form.getValues());
                                                  }}
                                                  pattern={REGEXP_ONLY_DIGITS}
                                        >
                                            <InputOTPGroup>
                                                <InputOTPSlot index={0}/>
                                                <InputOTPSlot index={1}/>
                                            </InputOTPGroup>
                                            {/*<InputOTPSeparator />*/}
                                            <InputOTPGroup>
                                                <InputOTPSlot index={2}/>
                                                <InputOTPSlot index={3}/>
                                                <InputOTPSlot index={4}/>
                                            </InputOTPGroup>
                                        </InputOTP>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="street"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Street</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your parking location street" {...field}
                                               onChange={(e) => {
                                                   field.onChange(e);
                                                   handleLocationChange("street", e.target.value, form.getValues());
                                               }}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="houseNumber"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>House Number</FormLabel>
                                    <FormControl>
                                        <Input
                                            placeholder="Please provide your parking location house number" {...field}
                                            onChange={(e) => {
                                                field.onChange(e);
                                                handleLocationChange("houseNumber", e.target.value, form.getValues());
                                            }}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <LocationMap /*apiKey={GOOGLE_MAPS_API_KEY}*/ location={location} coordinates={markerPosition}
                                                                      setCoorditnates={setMarkerPosition} markerIsDraggable={true}/>
                        <FormField
                            control={form.control}
                            name="belongToInstitution"
                            render={({field}) => (
                                <FormItem className=" flex flex-row items-center justify-start gap-3">
                                    <FormControl>
                                        <Checkbox
                                            checked={field.value}
                                            onCheckedChange={field.onChange}
                                        />
                                    </FormControl>
                                    <FormLabel className="text-left mt-1">Does the parking area belong to any
                                        institution (e.g., a shopping mall, park zone, or private parking)</FormLabel>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        {form.watch("belongToInstitution") && <FormField
                            control={form.control}
                            name="institutionName"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Institution name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide name of the institution" {...field}
                                               onChange={(e) => {
                                                   field.onChange(e);
                                               }}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />}
                        <FormField
                            control={form.control}
                            name="termsAccept"
                            render={({field}) => (
                                <FormItem className=" flex flex-row items-center justify-start gap-3">

                                    <FormControl className="mt-2">
                                        <Checkbox
                                            checked={field.value}
                                            onCheckedChange={field.onChange}
                                        />
                                    </FormControl>
                                    <FormLabel>Accept terms and condition </FormLabel>
                                    {/*<span className="mt-0">Terms Agreement</span>*/}
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>


                    <Button
                        type="submit"
                        className={`w-full ${
                            isFormValid ? "bg-neon hover:bg-neon-dark" : "bg-jordy hover:bg-gray-400"
                        }`}
                        disabled={!isFormValid} // Disable button when the form is invalid
                    >
                        Create Parking
                    </Button>

                </form>
            </Form>
        </div>
    )
}

export default CreateParkingStep1;
