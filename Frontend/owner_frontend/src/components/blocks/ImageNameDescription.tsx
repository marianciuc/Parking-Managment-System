import {IParking} from "@/models/common";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage
} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Context} from "@/main.tsx";
import React, {useContext, useState} from "react";
import axios from "axios";
import { cn } from "@/lib/utils";
import "./ImageNameDescription.css"
import {Upload} from "lucide-react";


const formSchema = z.object({
    image: z.string(),
    parkingName: z.string().min(2,{
        message: "Parking name must be at least 2 characters.",
    }),
    description: z.string().optional(),
});

function ImageNameDescription({parking}:{parking :IParking}) {

    const [image, setImage] = useState<string | null>(parking.imageUrl || null); // Картинка из parkingStore

    const {parkingStore} = useContext(Context);

    const defaultDescription = "Description";

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            image: parking.imageUrl,
            parkingName: parking.name,
            description: parking.description ?? defaultDescription,
        },
        mode: "onChange", // Enables immediate validation updates
    });

    async function onSubmit(values : z.infer<typeof formSchema>) {
        console.log(values);
         await parkingStore.updateParkingDetails({name: values.parkingName, description: values.description ?? null, imageUrl: null})
    }


    const isFormValid =form.formState.isValid && (parking.name !=form.watch("parkingName") || form.watch("description") !== defaultDescription );

    async function handleImageChange(event: React.ChangeEvent<HTMLInputElement>) {
        const file = event.target.files?.[0];
        if (file) {
            // Загрузка изображения на сторонний сервис
            const formData = new FormData();
            formData.append("file", file);
            formData.append("upload_preset", "your_upload_preset"); // Замени на свой пресет

            try {
                const response = await axios.post(
                    "https://api.cloudinary.com/v1_1/your_cloudinary_name/image/upload",
                    formData
                );
                setImage(response.data.secure_url); // Обновляем изображение
                form.setValue("image", response.data.secure_url); // Сохраняем URL в форму
            } catch (error) {
                console.error("Ошибка загрузки изображения:", error);
            }
        }
    }



    return (
        <div className="flex flex-col justify-start items-start w-full gap-[27px] h-[400px] mb-16">
            {/* Блок с загрузкой картинки */}

            <div
                className={cn(
                    "relative w-full h-[300px] shadow-lg border border-gray-200 rounded-lg overflow-hidden transition-all duration-500 ease-in-out",
                    image ? "bg-cover bg-center" : "bg-gray-100"
                )}
                style={image ? { backgroundImage: `url(${image})` } : undefined} // Устанавливаем фоновое изображение
            >
                {/* Если картинка отсутствует, показываем placeholder */}
                {!image && (
                    <div className="w-full h-full flex justify-center items-center text-gray-400 text-lg font-medium">
                        {/*Choose or upload image*/}
                        <img src="/logo_text_black.png" alt="Logo Image" />
                    </div>
                )}
                {/* Затемнение и кнопка загрузки при наведении */}
                <div className="absolute inset-0 flex items-end justify-end p-4 mask bg-opacity-40 opacity-0 hover:opacity-100 transition-opacity duration-500 ease-in-out">
                    <label className="w-1/4 relative">
                        <Input
                            type="file"
                            accept="image/*"
                            className="opacity-0 absolute w-full h-full cursor-pointer"
                            onChange={handleImageChange}
                        />
                        <Button variant="secondary" className="w-full bg-transparent border-none text-white right-10 cursor-pointer">
                            Upload new image
                            <Upload color="white"/>
                        </Button>
                    </label>
                </div>
            </div>


            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 w-full relative">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="parkingName"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Parking Name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Parking name" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="description"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Description</FormLabel>
                                    <FormControl>
                                        <textarea
                                            className="w-full border rounded p-2 active:border-card_color_open"

                                            placeholder="Provide a description here"
                                            {...field}
                                        />
                                    </FormControl>
                                    <FormDescription>
                                        Users can see your parking description
                                    </FormDescription>
                                    <FormMessage />
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

export default ImageNameDescription;