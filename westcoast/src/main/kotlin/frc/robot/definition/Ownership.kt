package frc.robot.definition

interface Ownership {
    class DoubleParameter<Owner>(val value: Double)
    interface ProviderDelegate<T> {
        operator fun invoke(): T
    }

    class Provider<T, Owner>(private val delegate: ProviderDelegate<T>) : ProviderDelegate<T> {
        override fun invoke(): T {
            return delegate.invoke()
        }
    }

    companion object {
        fun <Owner> parameter(value: Double): DoubleParameter<Owner> {
            return DoubleParameter(value)
        }
    }
}