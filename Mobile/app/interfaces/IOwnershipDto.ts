export default interface OwnershipDto {
    id: string;
    creationDate: string;
    modificationDate: string;
    recordStatus: string;
    vehicleId: string;
    ownerId: string;
    ownerType: string;
    version: number;
}