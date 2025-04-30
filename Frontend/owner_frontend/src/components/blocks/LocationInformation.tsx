import {IParking} from "@/models/common";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {SEditLocation} from "@/models/common/formSchemas";
import {countries} from "@/interfaces/StaticData.ts";
import {ILocation, IMarkerPosition, IQueryParts, IZoomLevels} from "@/models/common/googleMaps";
import axios from "axios";
import {useState, useContext} from "react";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {cn} from "@/lib/utils.ts";
import {Check, ChevronsUpDown} from "lucide-react";
import {Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {InputOTP, InputOTPGroup, InputOTPSlot} from "@/components/ui/input-otp.tsx";
import {REGEXP_ONLY_DIGITS} from "input-otp";
import LocationMap from "@/components/maps/locationMap.tsx";
import { Context } from "@/main";



const GOOGLE_MAPS_API_KEY = import.meta.env.VITE_REACT_APP_GOOGLE_MAPS_API_KEY as string;


function LocationInformation({parking}:{parking:IParking}) {

    const {parkingStore} = useContext(Context)

    const form = useForm<z.infer<typeof SEditLocation>>({
        resolver: zodResolver(SEditLocation),
        defaultValues: {
            country: parking.address.countryCode,
            city: parking.address.city,
            postalCode: parking.address.postalCode,
            street: parking.address.street,
            houseNumber: parking.address.buildingNumber,
            institutionName: parking.address.institutionName ? parking.address.institutionName : "",
        },
        mode: "onChange", // Enables immediate validation updates
    });

    const [location, setLocation] = useState<ILocation>({
        lat: parking.address.latitude,
        lng:parking.address.longitude,
        zoom:17,
    });
    const [markerPosition, setMarkerPosition] = useState<IMarkerPosition>({
        lat: parking.address.latitude,
        lng: parking.address.longitude,
    });


    //TODO : make this two function reusable (CreateParkingStep1)
    //google maps
    function getCountryNameByCode(code: string | undefined) {
        const country = countries.find(country => country.value === code);
        return country ? country.label : undefined;
    }


    const handleLocationChange = async (field:string, value:string | undefined , formValues: z.infer<typeof SEditLocation>  ) => {
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

    function onSubmit(values : z.infer<typeof SEditLocation>) {
        parkingStore.updateParkingLocation({
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
                isBelongToAnyInstitution: !!values.institutionName,
                institutionName: values.institutionName,

        }).then(response => {
            console.log(response);

        }).catch(error => {
            console.log(error);
        }).finally(()=>{
            console.log("finally")
        })
    }

    const isFormValid = form.formState.isValid;

    const isNoCreationProcess = parking.parkingStatus !== "CREATION_PROCESS";


    return(
        <div className="flex flex-col gap-10 h-[1100px]">
            <div className="flex flex-col justify-start items-start text-left gap-5 border-b-2 p-2 label-text">
                <h3>Location</h3>
                <p> The data provided in this block can only be changed when the parking lot is created. But you can always add the name of the nearest institution.</p>
            </div>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 relative ">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="country"
                            render={({field}) => (
                                <FormItem className={`text-left flex flex-col `}>
                                    <FormLabel>Country</FormLabel>
                                    <Popover>
                                        <PopoverTrigger asChild>
                                            <FormControl>
                                                <Button
                                                    disabled={isNoCreationProcess}
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
                                                    <CommandEmpty>No country.</CommandEmpty>
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
                                <FormItem className={`text-left w-full`}>
                                    <FormLabel>City</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your parking city"
                                               disabled={isNoCreationProcess}
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
                                <FormItem className={`text-left`}>
                                    <FormLabel>Postal Code</FormLabel>
                                    <FormControl>
                                        <InputOTP maxLength={5}{...field}
                                            disabled={isNoCreationProcess}
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
                                                <InputOTPSlot index={4} />
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
                                <FormItem className={`text-left `}>
                                    <FormLabel>Street</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your parking location street" {...field}
                                            disabled={isNoCreationProcess}
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
                                <FormItem className={`text-left `}>
                                    <FormLabel>House Number</FormLabel>
                                    <FormControl>
                                        <Input
                                            placeholder="Please provide your parking location house number" {...field}
                                            disabled={isNoCreationProcess}
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
                                                                      setCoorditnates={setMarkerPosition} markerIsDraggable={!isNoCreationProcess}/>

                         <FormField
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
                        />
                    </div>

                    <Button
                        type="submit"
                        className="bg-lime-600 self-end absolute right-0"
                        disabled={!isFormValid}
                    >
                        Save
                    </Button>

                </form>
            </Form>
        </div>
    )
}

export default LocationInformation;