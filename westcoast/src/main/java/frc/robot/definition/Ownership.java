package frc.robot.definition;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Ownership {
    final class DoubleParameter<Owner> {
        public final double value;

        public DoubleParameter(double value) {
            this.value = value;
        }
    }

    static <Owner> DoubleParameter<Owner> parameter(double value) {
        return new DoubleParameter<>(value);
    }


    interface ProviderDelegate<T> {
        T invoke();
    }

    final class Provider<T, Owner> implements ProviderDelegate<T> {
        @NotNull private final ProviderDelegate<T> delegate;

        public Provider(@NotNull ProviderDelegate<T> provider) {
            this.delegate = provider;
        }

        public T invoke() {
            return delegate.invoke();
        }
    }
}