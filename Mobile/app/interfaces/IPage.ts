export default interface IPage<T> {
    content: T[];
    pagable: {
        pageNumber: number, pageSize: number,
        sort: { empty: boolean, sorted: boolean, unsorted: boolean },
        offset: number,
        unpaged: boolean,
        paged: boolean
    };
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    sort: { empty: boolean, sorted: boolean, unsorted: boolean };
    numberOfElements: number;
    first: boolean;
    empty: boolean;

}