
export default interface Page<T> {
    content: T[],
    number: number,
    size: number,
    total: number
}