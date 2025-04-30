export enum OwnerType {
    COMPANY = "COMPANY",
    PERSON = "PERSON",
    FAMILY = "FAMILY",
}

export default interface IOwnership {
    ownerId: string,
    ownerType: OwnerType,
    creationDate: Array<number>;
    modificationDate: Array<number>;
}