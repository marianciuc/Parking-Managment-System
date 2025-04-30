package pl.edu.zut.app.parking.parking.dto.responses;

public record PaginationResponse<T>(
        Integer pages,
        Integer currentPage,
        Integer pageSize,
        T data
) {
}
