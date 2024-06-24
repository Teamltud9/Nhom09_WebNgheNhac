package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Premium;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.PremiumRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PremiumService {
    private final PremiumRepository premiumRepository;


    public List<Premium> getAll()
    {
        return premiumRepository.findAll();
    }
    public Optional<Premium> findById(int id)
    {
        return premiumRepository.findById(id);
    }
    public void add(Premium premium){
        premiumRepository.save(premium);
    }
    public void update(@NotNull Premium premium)
    {
        Premium existingPremium = premiumRepository.findById((int)premium.getPremiumId())
                .orElseThrow(() -> new IllegalStateException("Premium" +
                premium.getPremiumName() + " does not exist."));
        existingPremium.setPremiumName(premium.getPremiumName());
        existingPremium.setPrice(premium.getPrice());
        existingPremium.setDuration(premium.getDuration());
        existingPremium.setPrice(premium.getPrice());
        premiumRepository.save(existingPremium);
    }

    public void deleteById(int id)
    {
        if (!premiumRepository.existsById(id)) {
            throw new IllegalStateException("Premium" + id + " does not exist.");
        }
        premiumRepository.deleteById(id);
    }

}
