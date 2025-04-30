package pl.edu.zut.app.parking.parking.specifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.javafaker.Faker;
import jakarta.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.zut.app.parking.parking.entities.Parking;

@ExtendWith(MockitoExtension.class)
class ParkingSpecificationsTest {

  private Faker faker;

  @Mock private CriteriaBuilder criteriaBuilder;

  @Mock private CriteriaQuery<Object> criteriaQuery;

  @Mock private Root<Parking> root;

  @Mock private Path<String> expression;

  @Mock private Expression<Object> objectExpression;

  @Mock private Predicate predicate;

  @BeforeEach
  public void setUp() {
    faker = new Faker();
  }

  @Nested
  @DisplayName("Test whereNameContains")
  class WhereNameContainsTest {
    @Test
    @DisplayName("Should return null when name is null or blank")
    public void testNullOrBlankName() {
      // Arrange
      String nullName = null;
      String blankName = "   ";

      // Act
      var specNull = ParkingSpecifications.whereNameContains(nullName);
      var specBlank = ParkingSpecifications.whereNameContains(blankName);

      // Assert
      assertNull(specNull.toPredicate(root, criteriaQuery, criteriaBuilder));
      assertNull(specBlank.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Lot", "Garage"})
    @DisplayName("Should apply LIKE filtering when name is provided")
    public void testValidName(String inputName) {
      // Arrange
      when(root.<String>get("name")).thenReturn(expression);
      when(criteriaBuilder.lower(expression)).thenReturn(expression);
      when(criteriaBuilder.like(eq(expression), any(String.class))).thenReturn(predicate);
      var spec = ParkingSpecifications.whereNameContains(inputName);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1))
          .like(eq(expression), eq("%" + inputName.toLowerCase() + "%"));
    }
  }

  @Nested
  @DisplayName("Test whereIdEquals")
  class WhereIdEqualsTest {
    @Test
    @DisplayName("Should return null when id is null")
    public void testIdNull() {
      // Arrange
      UUID id = null;
      var spec = ParkingSpecifications.whereIdEquals(id);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNull(result);
    }

    @Test
    @DisplayName("Should apply equal filtering when id is provided")
    public void testValidId() {
      // Arrange
      UUID randomId = UUID.randomUUID();
      Path<Object> idPath = mock(Path.class);

      when(root.get("id")).thenReturn(idPath);
      when(criteriaBuilder.equal(idPath, randomId)).thenReturn(predicate);
      var spec = ParkingSpecifications.whereIdEquals(randomId);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1)).equal(idPath, randomId);
    }
  }

  @Nested
  @DisplayName("Test whereCountryCodeEquals")
  class WhereCountryCodeEqualsTest {
    @Test
    @DisplayName("Should return null when country code is null or blank")
    public void testNullOrBlankCountry() {
      // Arrange
      String nullCountry = null;
      String blankCountry = "  ";

      // Act
      var specNull = ParkingSpecifications.whereCountryCodeEquals(nullCountry);
      var specBlank = ParkingSpecifications.whereCountryCodeEquals(blankCountry);

      // Assert
      assertNull(specNull.toPredicate(root, criteriaQuery, criteriaBuilder));
      assertNull(specBlank.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    @Test
    @DisplayName("Should apply equal filtering when country code is provided")
    public void testValidCountryCode() {
      // Arrange
      String countryCode = faker.address().countryCode();
      Path<Object> addressPath = mock(Path.class);
      Path<String> countryPath = mock(Path.class);
      when(root.get("address")).thenReturn(addressPath);
      when(addressPath.get("countryCode")).thenReturn((Path) countryPath);
      when(criteriaBuilder.equal(countryPath, countryCode)).thenReturn(predicate);
      var spec = ParkingSpecifications.whereCountryCodeEquals(countryCode);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1)).equal(countryPath, countryCode);
    }
  }

  @Nested
  @DisplayName("Test hasCity")
  class HasCityTest {
    @Test
    @DisplayName("Should return null when city is null or blank")
    public void testNullOrBlankCity() {
      // Arrange
      String nullCity = null;
      String blankCity = "";

      // Act
      var specNull = ParkingSpecifications.hasCity(nullCity);
      var specBlank = ParkingSpecifications.hasCity(blankCity);

      // Assert
      assertNull(specNull.toPredicate(root, criteriaQuery, criteriaBuilder));
      assertNull(specBlank.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    @Test
    @DisplayName("Should apply equal filtering when city is provided")
    public void testValidCity() {
      // Arrange
      String city = faker.address().city();
      Path<Object> addressPath = mock(Path.class);
      Path<String> cityPath = mock(Path.class);
      when(root.get("address")).thenReturn(addressPath);
      when(addressPath.get("city")).thenReturn((Path) cityPath);
      when(criteriaBuilder.equal(cityPath, city)).thenReturn(predicate);
      var spec = ParkingSpecifications.hasCity(city);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1)).equal(cityPath, city);
    }
  }

  @Nested
  @DisplayName("Test whereTagIdIn")
  class WhereTagIdInTest {
    @Test
    @DisplayName("Should return null when tags list is null or empty")
    public void testNullOrEmptyTags() {
      // Arrange
      List<UUID> nullList = null;
      List<UUID> emptyList = Collections.emptyList();

      // Act
      var specNull = ParkingSpecifications.whereTagIdIn(nullList);
      var specEmpty = ParkingSpecifications.whereTagIdIn(emptyList);

      // Assert
      assertNull(specNull.toPredicate(root, criteriaQuery, criteriaBuilder));
      assertNull(specEmpty.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    @Test
    @DisplayName("Should apply IN filtering when tags list is provided")
    public void testValidTags() {
      // Arrange
      UUID tagId = UUID.randomUUID();
      List<UUID> tags = List.of(tagId);
      Join<Object, Object> joinMock = mock(Join.class);
      Path<Object> idPath = mock(Path.class);
      when(root.join("tags")).thenReturn(joinMock);
      when(joinMock.get("id")).thenReturn(idPath);
      when(idPath.in(tags)).thenReturn(predicate);
      var spec = ParkingSpecifications.whereTagIdIn(tags);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(idPath, times(1)).in(tags);
    }
  }

  @Nested
  @DisplayName("Test whereIsClosedEquals")
  class WhereIsClosedEqualsTest {
    @Test
    @DisplayName("Should return null when isOpen is null")
    public void testNullIsOpen() {
      // Arrange
      Boolean isOpen = null;
      var spec = ParkingSpecifications.whereIsClosedEquals(isOpen);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNull(result);
    }

    @ParameterizedTest
    @CsvSource({"true, false", "false, true"})
    @DisplayName("Should apply equal filtering based on isOpen flag")
    public void testIsOpenFiltering(Boolean isOpen, Boolean expectedIsClosed) {
      // Arrange
      Path<Object> isClosedPath = mock(Path.class);
      when(root.get("isClosed")).thenReturn(isClosedPath);
      when(criteriaBuilder.equal(isClosedPath, expectedIsClosed)).thenReturn(predicate);
      var spec = ParkingSpecifications.whereIsClosedEquals(isOpen);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1)).equal(isClosedPath, expectedIsClosed);
    }
  }

  @Nested
  @DisplayName("Test whereIs24hEquals")
  class WhereIs24hEqualsTest {
    @Test
    @DisplayName("Should return null when is24h is null")
    public void testNullIs24h() {
      // Arrange
      Boolean is24h = null;
      var spec = ParkingSpecifications.whereIs24hEquals(is24h);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNull(result);
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    @DisplayName("Should apply equal filtering for is24h")
    public void testIs24hFiltering(Boolean is24h) {
      // Arrange

      Path<Object> is24hPath = mock(Path.class);
      when(root.get("is24h")).thenReturn(is24hPath);
      when(criteriaBuilder.equal(is24hPath, is24h)).thenReturn(predicate);
      var spec = ParkingSpecifications.whereIs24hEquals(is24h);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, criteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(criteriaBuilder, times(1)).equal(is24hPath, is24h);
    }
  }

  @Nested
  @DisplayName("Test withinRadius")
  class WithinRadiusTest {

    @Test
    @DisplayName("Should return null when any input is NaN")
    public void testWithinRadiusReturnsNullForNaN() {
      // Arrange
      double validLat = 10.0;
      double validLon = 20.0;
      double validRadius = 1000.0;

      // Act & Assert
      var spec1 = ParkingSpecifications.withinRadius(Double.NaN, validLon, validRadius);
      assertNull(spec1.toPredicate(root, criteriaQuery, criteriaBuilder));

      var spec2 = ParkingSpecifications.withinRadius(validLat, Double.NaN, validRadius);
      assertNull(spec2.toPredicate(root, criteriaQuery, criteriaBuilder));

      var spec3 = ParkingSpecifications.withinRadius(validLat, validLon, Double.NaN);
      assertNull(spec3.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    @Test
    @DisplayName("Should produce predicate when valid inputs are provided")
    public void testWithinRadiusValid() {
      // Arrange
      double userLat = 10.0;
      double userLon = 20.0;
      double radius = 1000.0;
      // Create a deep stub CriteriaBuilder to handle chained calls
      CriteriaBuilder deepCriteriaBuilder = mock(CriteriaBuilder.class, RETURNS_DEEP_STUBS);
      // Stub the final lessThanOrEqualTo call to return our predicate mock
      when(deepCriteriaBuilder.lessThanOrEqualTo(any(), eq(radius))).thenReturn(predicate);

      var spec = ParkingSpecifications.withinRadius(userLat, userLon, radius);

      // Act
      Predicate result = spec.toPredicate(root, criteriaQuery, deepCriteriaBuilder);

      // Assert
      assertNotNull(result);
      verify(deepCriteriaBuilder, times(1)).lessThanOrEqualTo(any(), eq(radius));
    }
  }

}
